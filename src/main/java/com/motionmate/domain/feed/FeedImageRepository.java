package com.motionmate.domain.feed;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {

    @Query("SELECT i FROM FeedImage i WHERE i.feed.id IN :feedIds")
    List<FeedImage> findByFeedIds(@Param("feedIds")List<Long>feedIds);
}
