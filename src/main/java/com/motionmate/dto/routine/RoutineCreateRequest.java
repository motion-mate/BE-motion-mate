package com.motionmate.dto.routine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.List;

@Getter
public class RoutineCreateRequest {
    @JsonProperty("user_id")
    private Long userId;
    private String title;
    private List<RoutineRequestDto> exercises;
}