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
    public FeedResponseDto upload(FeedRequestDto request, Long userId) {
        User user = userRePository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

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
    public List<FeedResponseDto> getFeedsByCursor(Long lastFeedId, int size, Long userId) {
      boolean isLoggedIn =  (userId != null);

      Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

      List<Long> feedIds = repository.findFeedIds(lastFeedId, pageable);
      if (feedIds.isEmpty()) return List.of();

      List<Feed> feeds = repository.findFeedsWithUserAndProfile(feedIds);

      //이미지 조회
      List<FeedImage>  imageList = feedImageRepository.findByFeedIds(feedIds);
      Map<Long, List<FeedImage>> imageMap = imageList.stream().collect(Collectors.groupingBy(img -> img.getFeed().getId()));

      //좋아요 여부 일괄 조회
      Set<Long> likedFeedIds = (userId != null)
              ? new HashSet<>(feedLikeRepository.findLikedFeedIdByUserId(userId))
              : Collections.emptySet();

      //좋아요 수 일괄 조회
      Map<Long, Integer> likeCountMap = feedLikeRepository.countLikesByFeedIds(feedIds).stream()
              .collect(Collectors.toMap(
                      row -> (Long) row[0],
                      row -> ((Long) row[1]).intValue()
              ));

      //댓글 수 일괄 조회
      Map<Long, Integer> commentCountMap = feedCommentRepository.countByFeedIds(feedIds).stream()
              .collect(Collectors.toMap(
                      row -> (Long) row[0],
                      row -> ((Long) row[1]).intValue()
              ));

      return feeds.stream()
              .filter(feed ->{
                 FeedAccessType accessType = feed.getFeedAccessType();

                  // 전체 공개 피드는 누구나 볼 수 있음
                 if (accessType == FeedAccessType.PUBLIC) return true;

                  // 비회원은 퍼블릭만 조회 가능
                 if (!isLoggedIn) return false;

                  // 자신의 피드는 항상 볼 수 있음
                 if (Objects.equals(feed.getUser().getId(), userId)) return true;

                  // FOLLOWERS 피드 + 로그인 상태 → 팔로우한 경우만 볼 수 있음
                 return accessType != FeedAccessType.FOLLOWERS || followRepository.existsByFromUser_IdAndToUser_Id(userId, feed.getUser().getId());
              })
              .map(feed -> {
                  Long feedId = feed.getId();
                  boolean liked = likedFeedIds.contains(feedId);
                  int likeCount = likeCountMap.getOrDefault(feedId, 0);
                  int commentCount = commentCountMap.getOrDefault(feedId, 0);
                  boolean isFollowing = false;
                  if(isLoggedIn && !Objects.equals(userId, feed.getUser().getId())) {
                      isFollowing = followService.isFollowing(userId, feed.getUser().getId()).isFollowing();
                  }
                  return FeedMapper.fromEntity(feed, liked, likeCount, commentCount, isFollowing);
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
    private void deleteOldImgaeIfExists(Feed feed, FeedImage oldImage) {
        if (oldImage != null && oldImage.getBucketKey() != null && !oldImage.getBucketKey().isEmpty()) {
            s3ServiceUtils.deleteFile(oldImage.getBucketKey());
            feed.getImages().remove(oldImage);
        }
    }

    //이미지 처리 분기
    private void ImageUpdate(Feed feed, FeedImage oldImage, S3FileRequest newImage, Long userId) {
        if (newImage == null) {
            deleteOldImgaeIfExists(feed, oldImage);
            return;
        }
        if (newImage.bucketKey() != null && !newImage.bucketKey().isEmpty()) {
            deleteOldImgaeIfExists(feed, oldImage);
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
    public void delete(Long feedId, Long userId) {
        Feed feed = findFeed(feedId);
        validateWriter(feed, userId);

        feed.getImages().forEach(image -> {
            String bucketKey = image.getBucketKey();
            if (bucketKey != null && !bucketKey.isEmpty()) {
              s3ServiceUtils.deleteFile(bucketKey);
            }
        });
        repository.delete(feed);
    }

    //본인 피드 조회
    public List<FeedResponseDto> getMyFeeds(Long userId) {
     User user = userRePository.findById(userId)
             .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
     List<Feed> myFeeds = repository.findByUserIdOrderByIdDesc(userId);

     return myFeeds.stream()
             .map(feed -> {
                 int likeCount = feedLikeRepository.countByFeed(feed);
                 int commentCount = feedCommentRepository.countByFeed(feed);
                 boolean liked = feedLikeRepository.existsByFeedAndUser(feed, user);
                 return FeedMapper.fromEntityLikeMyFeed(feed, liked, likeCount, commentCount);
             })
             .toList();
    }

}
