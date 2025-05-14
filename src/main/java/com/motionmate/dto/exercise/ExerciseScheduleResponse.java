package com.motionmate.dto.exercise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseScheduleResponse {
    @JsonProperty("schedule_id")
    private Long scheduleId;
    @JsonProperty("exercise_name")
    private String exerciseName;
    @JsonProperty("exercise_category")
    private String exerciseCategory;

    private String date;

    @JsonProperty("set_number")
    private int setNumber;

    private Double kg;
    private Integer reps;
    private String time;
}
