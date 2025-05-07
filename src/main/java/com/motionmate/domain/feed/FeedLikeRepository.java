package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    //내가 좋아요 눌렀는지 여부
    boolean existsByFeedAndUser(Feed feed, User user);

    //해당 피드에 눌린 좋아요 수
    int countByFeed(Feed feed);

    //좋아요 엔터티 가져오기(취소용)
    Optional<FeedLike> findByFeedAndUser(Feed feed, User user);

    //좋아요 누른 피드 목록 조회
    List<FeedLike> findByUser(User user);
}
