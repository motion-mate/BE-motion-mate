package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    //내가 좋아요 눌렀는지 여부
    boolean existsByFeedAndUser(Feed feed, User user);

    //해당 피드에 눌린 좋아요 수
    int countByFeed(Feed feed);
}
