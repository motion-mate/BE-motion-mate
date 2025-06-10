package com.motionmate.domain.feed;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    //해당 피드에 달린 댓글 수
    int countByFeed(Feed feed);

    // 피드 ID 리스트에 따른 댓글 수 그룹핑
    @Query("SELECT fc.feed.id, COUNT(fc) FROM FeedComment fc WHERE fc.feed.id IN :feedIds GROUP BY fc.feed.id")
    List<Object[]> countByFeedIds (@Param("feedIds") List<Long> feedIds);

    // 댓글 단건 조회 with user + profile
    @Query("SELECT fc FROM FeedComment fc " +
            "JOIN FETCH fc.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE fc.id = :commentId")
    Optional<FeedComment> findWithUserProfileById(@Param("commentId") Long commentId);

    // 피드별 댓글 목록
    @Query("SELECT fc FROM FeedComment fc " +
            "JOIN FETCH fc.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE fc.feed.id = :feedId " +
            "ORDER BY fc.createdAt DESC")
    List<FeedComment> findWithUserProfileByFeedId(@Param("feedId") Long feedId);

    // 댓글 단건 조회 with user (JOIN)
    @Query("SELECT fc FROM FeedComment fc " +
            "JOIN FETCH fc.user u " +
            "JOIN FETCH u.profile " +
            "WHERE fc.id = :commentId")
    Optional<FeedComment> findWithUserById(@Param("commentId") Long commentId);

    // 피드 삭제 시 댓글 일괄 삭제용
    @Modifying
    @Transactional
    @Query("DELETE FROM FeedComment fc WHERE fc.feed.id = :feedId")
    void deleteByFeedId(@Param("feedId") Long feedId);

}
