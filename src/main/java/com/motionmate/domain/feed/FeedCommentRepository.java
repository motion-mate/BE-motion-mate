package com.motionmate.domain.feed;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    //해당 피드에 달린 댓글 수
    int countByFeed(Feed feed);
    List<FeedComment> findByFeed(Feed feed);
    List<FeedComment> findTop10ByFeedOrderByCreatedAtDesc(Feed feed);

}
