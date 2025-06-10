package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    //내가 좋아요 눌렀는지 여부
    boolean existsByFeedAndUser(Feed feed, User user);

    //단일 피드에 눌린 좋아요 수
    int countByFeed(Feed feed);

    //좋아요 엔터티 가져오기(취소용)
    Optional<FeedLike> findByFeedAndUser(Feed feed, User user);

    //여러 피드에 눌린 좋아요 수
    @Query("SELECT f.feed.id, COUNT(f) FROM FeedLike f WHERE f.feed.id IN :feedIds GROUP BY f.feed.id")
    List<Object[]> countLikesByFeedIds(@Param("feedIds") List<Long> feedIds);

    // 내가 누른 피드 ID 목록
    @Query("SELECT f.feed.id FROM FeedLike f WHERE f.user.id = :userId")
    List<Long> findLikedFeedIdByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM FeedLike fl WHERE fl.feed.id = :feedId")
    void deleteByFeedId(@Param("feedId") Long feedId);

}
