package com.motionmate.service.websocket;

import com.motionmate.dto.notification.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long toUserId, NotificationResponseDto notificationDto) {
        String destination = "/sub/notification/" + toUserId;
        messagingTemplate.convertAndSend(destination, notificationDto);
    }
}
