package com.motionmate.service.feed;

import com.motionmate.domain.feed.*;
import com.motionmate.domain.follow.FollowRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfileRepository;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.feed.FeedMapper;
import com.motionmate.mapper.s3.S3FileMapper;
import com.motionmate.service.follow.FollowService;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository repository;
    private final UserRepository userRePository;
    private final UserProfileRepository userProfileRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final FollowRepository followRepository;
    private final FollowService followService;
    private final S3ServiceUtils s3ServiceUtils;
    private final FeedImageRepository feedImageRepository;

    int userPk = 102;

    //피드 업로드
    public FeedResponseDto upload(FeedRequestDto request, User user) {

        S3FileRequest imageInfo = request.getImageUrl();

        if (imageInfo != null) {
            // 이미지 이동 후 정보 갱신
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(imageInfo, userPk);
            imageInfo = S3FileMapper.toS3FileRequest(moved);
            s3ServiceUtils.deleteUserTempFiles(userPk);
        }

        // 피드 생성
        Feed feed = FeedMapper.toEntity(
                FeedRequestDto.builder()
                        .imageUrl(imageInfo)
                        .description(request.getDescription())
                        .feedAccessType(request.getFeedAccessType())
                        .build(),
                user
        );

        // 이미지가 있으면 FeedImage 생성 및 추가
        if (imageInfo != null) {
            feed.addImage(FeedImage.builder()
                    .url(imageInfo.url())
                    .bucketKey(imageInfo.bucketKey())
                    .orgName(imageInfo.orgName())
                    .build());
        }

        return FeedMapper.fromEntity(repository.save(feed));
    }



    //전체 피드 조회
    @Transactional(readOnly = true)
    public List<FeedResponseDto> getFeedsByCursor(Long lastFeedId, int size, User user) {
        // 로그인 여부 체크
        boolean isLoggedIn = (user != null);
        Long userId = isLoggedIn ? user.getId() : null;

        // 커서 기반 페이징: 가장 마지막 피드 ID를 기준으로 최신 순 정렬 후 상위 size개 조회
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        // 먼저 페이징에 해당하는 피드 ID만 추출
        List<Long> feedIds = repository.findFeedIds(lastFeedId, pageable);
        if (feedIds.isEmpty()) return List.of();
        

        // Feed + User + Profile 정보를 JOIN FETCH로 조회하여 N+1 방지
        List<Feed> feeds = repository.findFeedsWithUserAndProfile(feedIds);

        // 피드에 포함된 이미지들을 feedId 기준으로 한 번에 조회 후 Map으로 구성
        List<FeedImage> imageList = feedImageRepository.findByFeedIds(feedIds);
        Map<Long, List<FeedImage>> imageMap = imageList.stream()
                .collect(Collectors.groupingBy(img -> img.getFeed().getId()));

        // 로그인한 유저가 좋아요 누른 피드 ID 리스트 조회 (N+1 방지)
        Set<Long> likedFeedIds = isLoggedIn
                ? new HashSet<>(feedLikeRepository.findLikedFeedIdByUserId(user.getId()))
                : Collections.emptySet();

        // 피드 좋아요 수 일괄 조회
        Map<Long, Integer> likeCountMap = feedLikeRepository.countLikesByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        // 피드 댓글 수 일괄 조회
        Map<Long, Integer> commentCountMap = feedCommentRepository.countByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        // 팔로우 여부
        Set<Long> followingIds = isLoggedIn
                ? new HashSet<>(followRepository.findFollowing(user.getId()))
                : Collections.emptySet();

        Map<Long, Feed> feedMap = feeds.stream().collect(Collectors.toMap(Feed::getId, feed -> feed));

        // 조회된 피드 리스트를 순회하며 응답 DTO로 변환
        return feedIds.stream()
                .map(feedMap::get)
                .filter(Objects::nonNull)
                // 각 피드의 공개 범위에 따라 필터링
                .filter(feed -> {
                    FeedAccessType accessType = feed.getFeedAccessType();
                    Long writerId = feed.getUser().getId();

                    // 전체 공개(PUBLIC)는 누구나 볼 수 있음
                    if (accessType == FeedAccessType.PUBLIC) return true;

                    if (!isLoggedIn) return false;

                    // 내가 작성한 피드라면 항상 볼 수 있음
                    if (Objects.equals(writerId,userId)) return true;

                    // FOLLOWERS 전용
                    return accessType != FeedAccessType.FOLLOWERS ||
                            followingIds.contains(writerId);
                })

                //
                .map(feed -> {
                    Long feedId = feed.getId();
                    Long writerId = feed.getUser().getId();

                    // 좋아요 여부
                    boolean liked = likedFeedIds.contains(feedId);

                    // 좋아요 수, 댓글 수
                    int likeCount = likeCountMap.getOrDefault(feedId, 0);
                    int commentCount = commentCountMap.getOrDefault(feedId, 0);

                    // 팔로우 여부 (작성자가 본인이 아닌 경우에만 체크)
                    boolean isFollowing = isLoggedIn &&
                            !Objects.equals(userId, writerId) &&
                            followingIds.contains(writerId);

                    // 이미지 URL 추출
                    String imageUrl = imageMap.getOrDefault(feedId, List.of())
                            .stream().findFirst()
                            .map(FeedImage::getUrl)
                            .orElse(null);

                    // 최종 DTO로 변환
                    return FeedMapper.fromEntity(feed, liked, likeCount, commentCount, isFollowing, imageUrl);
                })
                .toList();
    }

    //피드 상세 페이지
    public FeedDetailResponseDto getFeedDetail(Long feedId, User user) {
        Feed feed = findFeed(feedId);
        Long userId = (user != null) ? user.getId() : null;
        FeedAccessType accessType = feed.getFeedAccessType();

        boolean isAuthor = userId != null && Objects.equals(feed.getUser().getId(), userId);
        boolean isFollowing = false;

        if (userId != null && !isAuthor) {
            isFollowing = followRepository.existsByFromUser_IdAndToUser_Id(userId, feed.getUser().getId());
        }

        if (accessType == FeedAccessType.FOLLOWERS && !isAuthor && !isFollowing) {
            throw new CustomException(HttpStatus.FORBIDDEN, "해당 피드에 접근할 수 없습니다.");
        }

        boolean liked = false;
        if (user != null) {
            liked = feedLikeRepository.existsByFeedAndUser(feed, user);
        }

        int likeCount = feedLikeRepository.countByFeed(feed);
        int commentCount = feedCommentRepository.countByFeed(feed);

        return FeedMapper.fromEntityDetail(feed, liked, likeCount, commentCount, isFollowing, userId);
    }


    //피드 수정
    @Transactional
    public FeedDetailResponseDto update(Long feedId, FeedRequestDto request, User user) {
       Feed feed = findFeed(feedId);
       validateWriter(feed, user.getId());

       FeedImage oldImage = feed.getImages().stream().findFirst().orElse(null);
       imageUpdate(feed, oldImage, request.getImageUrl(), user.getId());
       feed.update(request.getDescription(), request.getFeedAccessType());

       return mapToDetailResponse(feed, user);

    }

    //피드 찾기(수정,삭제용)
    private Feed findFeed(Long feedId) {
        return repository.findFeedWithUserAndImage(feedId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 피드를 찾을 수 없습니다"));
    }

    //작성자 확인
    private void validateWriter(Feed feed, Long userId) {
       if (!feed.getUser().getId().equals(userId)) {
           throw new CustomException(HttpStatus.FORBIDDEN, "수정권한이 없습니다");
       }
    }

    //이미지 삭제
    private void deleteOldImageIfExists(Feed feed, FeedImage oldImage) {
        if (oldImage != null && oldImage.getBucketKey() != null && !oldImage.getBucketKey().isEmpty()) {
            s3ServiceUtils.deleteFile(oldImage.getBucketKey());
            feed.getImages().remove(oldImage);
        }
    }

    //이미지 처리 분기
    private void imageUpdate(Feed feed, FeedImage oldImage, S3FileRequest newImage, Long userId) {
        //이미지 유지
        if (newImage == null) {
            return;
        }
        //수정 시 이미지 삭제
        if ((newImage.bucketKey() == null || newImage.bucketKey().isEmpty()) && oldImage != null) {
            deleteOldImageIfExists(feed, oldImage);
            return;
        }
        //이미지 교체
        if (newImage.bucketKey() != null && !newImage.bucketKey().isEmpty()) {
            deleteOldImageIfExists(feed, oldImage);
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(newImage, userPk);
            FeedImage updated = FeedImage.builder()
                    .url(moved.url())
                    .bucketKey(moved.bucketKey())
                    .orgName(moved.orgName())
                    .build();
            feed.addImage(updated);
        }
    }
    //응답 DTO 구성
    private FeedDetailResponseDto mapToDetailResponse(Feed feed, User user) {
        boolean liked = feedLikeRepository.existsByFeedAndUser(feed, user);
        int likeCount = feedLikeRepository.countByFeed(feed);
        int commentCount = feedCommentRepository.countByFeed(feed);
        boolean isFollowing = false;

        return FeedMapper.fromEntityDetail(feed, liked, likeCount, commentCount, isFollowing, user.getId());
    }

    //피드 삭제
    @Transactional
    public void delete(Long feedId, User user) {
        Feed feed = repository.findById(feedId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다"));

        if (!Objects.equals(feed.getUser().getId(), user.getId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다");
        }

        feed.getImages().forEach(image -> {
            String key = image.getBucketKey();
            if ( key != null && !key.isEmpty()) {
                s3ServiceUtils.deleteFile(key);
            }
        });

        feedCommentRepository.deleteByFeedId(feedId);
        feedLikeRepository.deleteByFeedId(feedId);
        repository.delete(feed);
    }

    //내 피드 조회
    public List<FeedResponseDto> getMyFeeds(User loginUser) {
        Long userId = loginUser.getId();

        List<Feed> myFeeds = repository.findFeedsWithUserAndProfileByUserId(userId);
        if (myFeeds.isEmpty()) return List.of();

        return mapFeedsToDtoWithLikesComments(myFeeds, loginUser.getId());
    }


    @Transactional(readOnly = true)
    public List<FeedResponseDto> getAllFeeds() {
        List<Feed> feeds = repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return feeds.stream()
                .map(FeedMapper::fromEntity)  // 좋아요/댓글 개수 등 불필요, 간단 출력
                .toList();
    }

    public List<FeedResponseDto> getUserFeed(Long targetUserId, Long userId) {
        User targetUser  = userRePository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."));

        List<Feed> userFeeds = repository.findAllByUserIdOrderByCreatedAtDesc(targetUserId);

        return mapFeedsToDtoWithLikesComments(userFeeds, userId);
    }

    private List<FeedResponseDto> mapFeedsToDtoWithLikesComments(
            List<Feed> feeds, Long userId) {

        if (feeds.isEmpty()) return List.of();

        List<Long> feedIds = feeds.stream()
                .map(Feed::getId)
                .collect(Collectors.toList());

        Map<Long, Integer> likeCountMap = feedLikeRepository.countLikesByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        Map<Long, Integer> commentCountMap = feedCommentRepository.countByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        Set<Long> likedFeedIds = new HashSet<>(feedLikeRepository.findLikedFeedIdByUserId(userId));

        return feeds.stream()
                .map(feed -> {
                    Long feedId = feed.getId();
                    int likeCount = likeCountMap.getOrDefault(feedId, 0);
                    int commentCount = commentCountMap.getOrDefault(feedId, 0);
                    boolean liked = likedFeedIds.contains(feedId);
                    return FeedMapper.fromEntityLikeMyFeed(feed, liked, likeCount, commentCount);
                })
                .collect(Collectors.toList());
    }

}
