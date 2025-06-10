package com.motionmate.dto.exercise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCompletedResponse {
    private String date;
    @JsonProperty("exercise_category")
    private String exerciseCategory;
    @JsonProperty("exercise_name")
    private String exerciseName;
    @JsonProperty("set_number")
    private int setNumber;
    private int kg;
    private int reps;
    private String time;
}
