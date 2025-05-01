package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {}