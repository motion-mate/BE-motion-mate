package com.motionmate.controller;

import com.motionmate.domain.notification.Notification;
import com.motionmate.domain.user.User;
import com.motionmate.dto.notification.NotificationRequestDto;
import com.motionmate.dto.notification.NotificationResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.mapper.NotificationMapper;
import com.motionmate.service.NotificationService;
import com.motionmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 생성
    @PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(@RequestBody NotificationRequestDto requestDto) {
        Notification notification = notificationService.createNotification(requestDto);
        NotificationResponseDto responseDto = NotificationMapper.toDto(notification);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 알림 조회
    @GetMapping(("/{userId}"))
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(
            @AuthenticationPrincipal CustomOAuth2User oauthUser,
            @PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationByUser(userId);
        List<NotificationResponseDto> responseDto = notifications.stream()
                .map(NotificationMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDto);
    }

    // 알림 읽음 처리 API (PATCH 요청)
    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<NotificationResponseDto> markAsRead(@PathVariable Long notificationId) {
        // NotificationService 에서 알림을 읽음으로 처리
        Notification updated = notificationService.markNotificationAsRead(notificationId);
        // 업데이트된 알림을 NotificationResponseDto 로 변환 후 응답
        NotificationResponseDto responseDto = NotificationMapper.toDto(updated);

        return ResponseEntity.ok(responseDto);
    }

     // 알림 전체 읽음처리
    @PatchMapping("/readAll/{userId}")
    public ResponseEntity<Void> markAsReadAll(@PathVariable Long userId) {
        notificationService.markNotificationAsReadAll(userId);

        return ResponseEntity.ok().build();
    }

    // 알림 단일삭제 요청
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);

        return ResponseEntity.noContent().build();
    }

    // 알림 전체삭제 요청
    @DeleteMapping("/deleteAll/{userId}")
    public ResponseEntity<Void> deleteAllNotifications(@PathVariable Long userId) {
        notificationService.deleteAllNotification(userId);

        return ResponseEntity.noContent().build();
    }
}
