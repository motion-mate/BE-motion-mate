package com.motionmate.dto.routine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RoutineGroupResponse {
    private String title;
    private List<RoutineResponseDto> exercises;
}
