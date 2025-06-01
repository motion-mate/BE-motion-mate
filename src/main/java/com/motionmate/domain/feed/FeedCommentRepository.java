package com.motionmate.domain.feed;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    //해당 피드에 달린 댓글 수
    int countByFeed(Feed feed);

    @EntityGraph(attributePaths = {"user", "user.profile"})
    List<FeedComment> findByFeedOrderByCreatedAtDesc(Feed feed);

    List<FeedComment> findTop10ByFeedOrderByCreatedAtDesc(Feed feed);

    @Query("SELECT fc.feed.id, COUNT(fc) FROM FeedComment fc WHERE fc.feed.id IN :feedIds GROUP BY fc.feed.id")
    List<Object[]> countByFeedIds (@Param("feedIds") List<Long> feedIds);

}
