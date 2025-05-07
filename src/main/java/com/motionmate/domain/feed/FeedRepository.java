package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByIdLessThanOrderByIdDesc(Long lastFeedId, Pageable pageable);
}