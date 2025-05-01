package com.motionmate.service;

import com.motionmate.domain.notification.Notification;
import com.motionmate.dto.notification.NotificationRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    public Notification createNotification(NotificationRequestDto requestDto) {
    }

    public List<Notification> getNotificationByUserId(Long userId) {
    }

    public Notification markNotificationAsRead(Long notificationId) {
    }

    public void deleteNotification(Long notificationId) {
    }
}
