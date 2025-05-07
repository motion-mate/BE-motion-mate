package com.motionmate.domain.notification;

import com.motionmate.domain.user.User;
import com.motionmate.global.oauth.CustomOAuth2User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByUserIdAndIsRead(Long userId, boolean isRead);

    List<Notification> findByUserId(Long userId);
}
