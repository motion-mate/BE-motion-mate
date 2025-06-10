package com.motionmate.dto.routine;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.time.LocalTime;

@Getter
public class RoutineRequestDto {
    @JsonProperty("exercise_id")
    private Long exerciseId;
    @JsonProperty("set_number")
    private int setNumber;
    private int kg;
    private int reps;
    private String time;
}