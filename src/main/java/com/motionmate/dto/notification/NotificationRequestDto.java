package com.motionmate.dto.notification;

import com.motionmate.domain.notification.Notification;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequestDto {

    @NotNull
    private Notification.NotificationType type;
    private Long userId;
    private String content;
}
