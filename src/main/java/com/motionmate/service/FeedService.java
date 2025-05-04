package com.motionmate.service;

import com.motionmate.domain.feed.*;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.mapper.FeedMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository repository;
    private final UserRepository userRePository;
    private final FeedLikeRepository feedLikeRepository;
    private final FeedCommentRepository feedCommentRepository;
    //피드 업로드
    public FeedResponseDto upload(FeedRequestDto request, Long userId) {
        User user = userRePository.findById(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        Feed saved = repository.save(FeedMapper.toEntity(request, user));
        return FeedMapper.fromEntity(saved);
    }

    //전체 피드 조회
    public List<FeedResponseDto> getAllFeed(Long userId ) {
        final User user = (userId != null) ? userRePository.findById(userId).orElse(null) : null;

        return repository.findAll().stream()
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

        User user = null;
        if(userId != null){
            user = userRePository.findById(userId)
                    .orElse(null);
        }

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

        boolean liked = feedLikeRepository.existsByFeedAndUser(feed, feed.getUser());
        int likeCount = feedLikeRepository.countByFeed(feed);
        int commentCount = feedCommentRepository.countByFeed(feed);

        return FeedMapper.fromEntity(feed, liked, likeCount, commentCount);
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
