package com.motionmate.service;

import com.motionmate.domain.feed.*;
import com.motionmate.domain.follow.FollowRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.mapper.FeedMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository repository;
    private final UserRepository userRePository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedCommentRepository feedCommentRepository;
    private final FollowRepository followRepository;

    //피드 업로드
    public FeedResponseDto upload(FeedRequestDto request, Long userId) {
        User user = userRePository.findById(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        Feed saved = repository.save(FeedMapper.toEntity(request, user));
        return FeedMapper.fromEntity(saved);
    }

    //전체 피드 조회
    public List<FeedResponseDto> getFeedsByCursor(Long lastFeedId, int size, Long userId) {
      User user =  (userId != null) ? userRePository.findById(userId).orElse(null) : null;

      Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
      List<Feed> feeds;

      if(lastFeedId == null) {
          feeds = repository.findAll(pageable).getContent();
      } else {
         feeds = repository.findByIdLessThanOrderByIdDesc(lastFeedId, pageable);
      }

      return feeds.stream()
              .filter(feed ->{
                 FeedAccessType accessType = feed.getFeedAccessType();

                  // 전체 공개 피드는 누구나 볼 수 있음
                 if (accessType == FeedAccessType.PUBLIC) return true;

                  // FOLLOWERS 피드인데 비로그인인 경우는 볼 수 없음
                 if (accessType == FeedAccessType.FOLLOWERS && userId == null) return false;

                  // 자신의 피드는 항상 볼 수 있음
                 if (Objects.equals(feed.getUser().getId(), userId)) return true;

                  // FOLLOWERS 피드 + 로그인 상태 → 팔로우한 경우만 볼 수 있음
                 if (accessType == FeedAccessType.FOLLOWERS) {
                     return followRepository.existsByFromUser_IdAndToUser_Id(userId, feed.getUser().getId());
                 }
                 return false;
              })
              .map(feed -> {
          boolean liked = (user != null) && feedLikeRepository.existsByFeedAndUser(feed, user);
          return FeedMapper.fromEntity(feed, liked);
      })
              .toList();
    }

    //피드 상세 페이지
    public FeedDetailResponseDto getFeedDetail(Long feedId,Long userId) {
        Feed feed = repository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "해당 피드를 찾을 수 없습니다."));

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

        return FeedMapper.fromEntity(feed, liked, likeCount, commentCount);
    }

    //피드 수정
    @Transactional
    public FeedDetailResponseDto update(Long feedId, FeedRequestDto request, Long userId) {
        Feed feed = repository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND ,"해당 피드를 찾을 수 없습니다."));

        //작성자 확인
        if(!feed.getUser().getId().equals(userId)){
            throw new CustomException(HttpStatus.FORBIDDEN ,"수정 권한이 없습니다.");
        }

        //설명 수정
        feed.updateDescription(request.getDescription());

       Feed updated = repository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "수정 후 피드를 다시 불러오지 못했습니다."));

        boolean liked = feedLikeRepository.existsByFeedAndUser(updated, updated.getUser());
        int likeCount = feedLikeRepository.countByFeed(updated);
        int commentCount = feedCommentRepository.countByFeed(updated);

        return FeedMapper.fromEntity(updated, liked, likeCount, commentCount);
    }

    //피드 삭제
    @Transactional
    public void delete(Long feedId, Long userId) {
        Feed feed = repository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"해당 피드를 찾을 수 없습니다."));

        if(!feed.getUser().getId().equals(userId)){
            throw new CustomException(HttpStatus.FORBIDDEN,"삭제 권한이 없습니다.");
        }
        repository.delete(feed);
    }

    //좋아요 누른 피드 조회
    public List<FeedResponseDto> getFeedsLikedByUser(Long userId){
        User user = userRePository.findById(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        List<FeedLike> likeFeeds = feedLikeRepository.findByUser(user);

        return likeFeeds.stream()
                .map(feedLike -> FeedMapper.fromEntity(feedLike.getFeed(), true)).toList();
    }
}
