package com.motionmate.dto.notification;

import lombok.Getter;

@Getter
public class NotificationRequestDto {

    private Long userId;
    private String type;
    private String content;
}
