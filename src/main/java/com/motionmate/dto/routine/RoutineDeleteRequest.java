package com.motionmate.dto.routine;

import lombok.Getter;

@Getter
public class RoutineDeleteRequest {
    private Long userId;
    private String title;
}
