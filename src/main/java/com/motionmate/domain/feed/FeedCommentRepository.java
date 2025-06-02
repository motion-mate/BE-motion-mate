package com.motionmate.domain.feed;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    //해당 피드에 달린 댓글 수
    int countByFeed(Feed feed);
    List<FeedComment> findByFeedOrderByCreatedAtDesc(Feed feed);
    List<FeedComment> findTop10ByFeedOrderByCreatedAtDesc(Feed feed);


    @Query("SELECT fc.feed.id, COUNT(fc) FROM FeedComment fc WHERE fc.feed.id IN :feedIds GROUP BY fc.feed.id")
    List<Object[]> countByFeedIds (@Param("feedIds") List<Long> feedIds);

    @Query("SELECT fc FROM FeedComment fc " +
            "JOIN FETCH fc.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE fc.id = :commentId")
    Optional<FeedComment> findWithUserProfileById(@Param("commentId") Long commentId);

    @Query("SELECT fc FROM FeedComment fc " +
            "JOIN FETCH fc.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE fc.feed.id = :feedId " +
            "ORDER BY fc.createdAt DESC")
    List<FeedComment> findWithUserProfileByFeedId(@Param("feedId") Long feedId);




}
