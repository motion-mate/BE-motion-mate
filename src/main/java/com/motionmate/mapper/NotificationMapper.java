package com.motionmate.mapper;

import com.motionmate.domain.notification.Notification;
import com.motionmate.domain.user.User;
import com.motionmate.dto.notification.NotificationRequestDto;
import com.motionmate.dto.notification.NotificationResponseDto;
import com.motionmate.service.UserService;

public class NotificationMapper {

    public static NotificationResponseDto toDto(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .type(notification.getType().name())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static Notification toEntity(NotificationRequestDto requestDto, User user) {
        return Notification.builder()
                .user(user)
                .type(requestDto.getType())
                .content(requestDto.getContent())
                .build();
    }
}
