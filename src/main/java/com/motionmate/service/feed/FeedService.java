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

        // 피드별 좋아요 수를 Group By로 한 번에 조회
        Map<Long, Integer> likeCountMap = feedLikeRepository.countLikesByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        // 피드별 댓글 수를 Group By로 한 번에 조회
        Map<Long, Integer> commentCountMap = feedCommentRepository.countByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        // 조회된 피드 리스트를 순회하며 응답 DTO로 변환
        return feeds.stream()
                // 각 피드의 공개 범위에 따라 필터링
                .filter(feed -> {
                    FeedAccessType accessType = feed.getFeedAccessType();

                    // 전체 공개(PUBLIC)는 누구나 볼 수 있음
                    if (accessType == FeedAccessType.PUBLIC) return true;


                    if (!isLoggedIn) return false;

                    // 내가 작성한 피드라면 항상 볼 수 있음
                    if (Objects.equals(feed.getUser().getId(), user.getId())) return true;

                    // FOLLOWERS 전용
                    return accessType != FeedAccessType.FOLLOWERS ||
                            followRepository.existsByFromUser_IdAndToUser_Id(user.getId(), feed.getUser().getId());
                })

                //
                .map(feed -> {
                    Long feedId = feed.getId();

                    // 좋아요 여부
                    boolean liked = likedFeedIds.contains(feedId);

                    // 좋아요 수, 댓글 수
                    int likeCount = likeCountMap.getOrDefault(feedId, 0);
                    int commentCount = commentCountMap.getOrDefault(feedId, 0);

                    // 팔로우 여부 (작성자가 본인이 아닌 경우에만 체크)
                    boolean isFollowing = false;
                    if (isLoggedIn && !Objects.equals(user.getId(), feed.getUser().getId())) {
                        isFollowing = followService.isFollowing(user.getId(), feed.getUser().getId()).isFollowing();
                    }

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
    public FeedDetailResponseDto getFeedDetail(Long feedId,Long userId) {
        Feed feed = findFeed(feedId);

        FeedAccessType accessType = feed.getFeedAccessType();

        if (accessType == FeedAccessType.FOLLOWERS) {
            boolean isAuthor = userId != null && Objects.equals(feed.getUser().getId(), userId);
            boolean isFollowing = userId != null && followRepository.existsByFromUser_IdAndToUser_Id(userId, feed.getUser().getId());

            if (!isAuthor && !isFollowing) {
                throw new CustomException(HttpStatus.FORBIDDEN, "해당 피드에 접근할 수 없습니다.");
            }
        }

        User user = (userId != null) ? userRePository.findById(userId).orElse(null) : null;

        boolean liked = false;
        if(user != null){
            liked = feedLikeRepository.existsByFeedAndUser(feed, user);
        }

        int likeCount = feedLikeRepository.countByFeed(feed);
        int commentCount = feedCommentRepository.countByFeed(feed);

        boolean isFollowing = false;
        if(user != null && !Objects.equals(user.getId(), feed.getUser().getId())) {
            isFollowing = followService.isFollowing(user.getId(), feed.getUser().getId()).isFollowing();
        }

        return FeedMapper.fromEntityDetail(feed, liked, likeCount, commentCount, isFollowing, userId);
    }

    //피드 수정
    @Transactional
    public FeedDetailResponseDto update(Long feedId, FeedRequestDto request, Long userId) {
       Feed feed = findFeed(feedId);
       validateWriter(feed, userId);

       FeedImage oldImage = feed.getImages().stream().findFirst().orElse(null);
       ImageUpdate(feed, oldImage, request.getImageUrl(), userId);
       feed.update(request.getDescription(), request.getFeedAccessType());

       return mapToDetailResponse(feed, userId);

    }

    //피드 찾기(수정,삭제용)
    private Feed findFeed(Long feedId) {
        return repository.findById(feedId).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "해당 피드를 찾을 수 없습니다"));
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
    private void ImageUpdate(Feed feed, FeedImage oldImage, S3FileRequest newImage, Long userId) {
        if (newImage == null) {
            deleteOldImageIfExists(feed, oldImage);
            return;
        }
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
    private FeedDetailResponseDto mapToDetailResponse(Feed feed, Long userId) {
        boolean liked = feedLikeRepository.existsByFeedAndUser(feed, feed.getUser());
        int likeCount = feedLikeRepository.countByFeed(feed);
        int commentCount = feedCommentRepository.countByFeed(feed);
        boolean isFollowing = false;

        return FeedMapper.fromEntityDetail(feed, liked, likeCount, commentCount, isFollowing, userId);
    }

    //피드 삭제
    @Transactional
    public void delete(Long feedId, User user) {
       List<String> bucketKeys = feedImageRepository.findBucketKeysByFeedId(feedId);
       bucketKeys.forEach(key -> {
           if (key != null && !key.isEmpty()) {
               s3ServiceUtils.deleteFile(key);
           }
       });

       Long writerId = repository.findWriterIdByFeedId(feedId);
       if(!Objects.equals(writerId, user.getId())) {
           throw new CustomException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다");
       }
       repository.deleteById(feedId);
    }

    //본인 피드 조회
    public List<FeedResponseDto> getMyFeeds(User loginUser) {
        Long userId = loginUser.getId();

        List<Feed> myFeeds = repository.findFeedsWithUserAndProfileByUserId(userId);
        if (myFeeds.isEmpty()) return List.of();

        // feedId 목록 추출
        List<Long> feedIds = myFeeds.stream()
                .map(Feed::getId)
                .toList();

        //  좋아요 수 일괄 조회
        Map<Long, Integer> likeCountMap = feedLikeRepository.countLikesByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        // 댓글 수 일괄 조회
        Map<Long, Integer> commentCountMap = feedCommentRepository.countByFeedIds(feedIds).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> ((Long) row[1]).intValue()
                ));

        // 좋아요 여부 일괄 조회
        Set<Long> likedFeedIds = new HashSet<>(feedLikeRepository.findLikedFeedIdByUserId(userId));

        // 매핑
        return myFeeds.stream()
                .map(feed -> {
                    Long feedId = feed.getId();
                    int likeCount = likeCountMap.getOrDefault(feedId, 0);
                    int commentCount = commentCountMap.getOrDefault(feedId, 0);
                    boolean liked = likedFeedIds.contains(feedId);
                    return FeedMapper.fromEntityLikeMyFeed(feed, liked, likeCount, commentCount);
                })
                .toList();
    }


    @Transactional(readOnly = true)
    public List<FeedResponseDto> getAllFeeds() {
        List<Feed> feeds = repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return feeds.stream()
                .map(FeedMapper::fromEntity)  // 좋아요/댓글 개수 등 불필요, 간단 출력
                .toList();
    }

}
