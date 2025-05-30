package com.motionmate.domain.notification;

import com.motionmate.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByUserIdAndIsRead(Long userId, boolean isRead);

    List<Notification> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("update Notification n set n.isRead = true where n.user.id = :userId and n.isRead = false")
    int markAllAsReadByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);

}
