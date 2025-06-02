package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    //커서 기반 페이징용 메서드

    List<Feed> findByUserIdOrderByIdDesc(Long userId);

    @Query("SELECT f.id FROM Feed f WHERE (:lastFeedId IS NULL OR f.id < :lastFeedId) ORDER BY f.id DESC")
    List<Long> findFeedIds(@Param("lastFeedId") Long lastFeedId, Pageable pageable);


    @Query("SELECT DISTINCT f FROM Feed f " +
            "JOIN FETCH f.user u " +
            "JOIN FETCH u.profile " +
            "WHERE f.id IN :ids")
    List<Feed> findFeedsWithUserAndProfile(@Param("ids") List<Long> ids);





}