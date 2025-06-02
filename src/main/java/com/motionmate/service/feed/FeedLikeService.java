package com.motionmate.service.feed;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedLike;
import com.motionmate.domain.feed.FeedLikeRepository;
import com.motionmate.domain.feed.FeedRepository;
import com.motionmate.domain.notification.Notification;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.notification.NotificationRequestDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedRepository feedRepository;
    private final FeedLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    //좋아요 토글(추가, 삭제)
    public boolean toggleLike(Long feedId, Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지않습니다."));


        //좋아요를 눌렀는지 확인
        Optional<FeedLike> existLike = likeRepository.findByFeedAndUser(feed, user);

        // 이미 눌렀으면 삭제처리(취소)
        if(existLike.isPresent()){
            likeRepository.delete(existLike.get());
            return false;
        } else {
            FeedLike newLike = new FeedLike(user, feed);
            likeRepository.save(newLike);

            if(!feed.getUser().getId().equals(userId)) {
                notificationService.createNotification(
                        NotificationRequestDto.builder()
                                .userId(feed.getUser().getId())
                                .type(Notification.NotificationType.LIKE)
                                .content(user.getProfile().getNickname() + "님이 회원님의 게시글을 좋아합니다.")
                                .build()
                );
            }
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
