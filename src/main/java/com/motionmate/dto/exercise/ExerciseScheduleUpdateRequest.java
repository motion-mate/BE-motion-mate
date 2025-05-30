package com.motionmate.dto.exercise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseScheduleUpdateRequest {
    private String date;
    @JsonProperty("exercise_name")
    private String exerciseName;
    @JsonProperty("exercise_category")
    private String exerciseCategory;
    @JsonProperty("set_number")
    private int setNumber;
    private Double kg;
    private int reps;
    private String time;

    @JsonProperty("user_id")
    private Long userId;
}
