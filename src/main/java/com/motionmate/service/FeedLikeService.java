package com.motionmate.service;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedLike;
import com.motionmate.domain.feed.FeedLikeRepository;
import com.motionmate.domain.feed.FeedRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedRepository feedRepository;
    private final FeedLikeRepository likeRepository;
    private final UserRepository userRepository;

    //좋아요 토글(추가, 삭제)
    public boolean toggleLike(Long feedId, Long userId){
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "로그인이 필요합니다."));

        Optional<FeedLike> existLike = likeRepository.findByFeedAndUser(feed, user);

        //좋아요를 눌렀는지 확인
        // 이미 눌렀으면 삭제처리(취소)
        if(existLike.isPresent()){
            likeRepository.delete(existLike.get());
            return false;
        } else {
            FeedLike newLike = new FeedLike(user, feed);
            likeRepository.save(newLike);
            return  true;
        }
    }

    //좋아요 수 확인
    public int getLikeCount(Long feedId){
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지않습니다."));
        return likeRepository.countByFeed(feed);
    }

    //좋아요 여부 확인
    public boolean isLiked(Long feedId, Long userId){
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        return likeRepository.existsByFeedAndUser(feed, user);
    }


}
