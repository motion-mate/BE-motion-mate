package com.motionmate.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private Long userId;
    private String type;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
}
