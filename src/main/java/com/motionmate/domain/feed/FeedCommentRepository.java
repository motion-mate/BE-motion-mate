package com.motionmate.domain.feed;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    //해당 피드에 달린 댓글 수
    int countByFeed(Feed feed);
}
