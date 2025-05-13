package com.motionmate.dto.exercise;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.motionmate.domain.exercise.ExerciseList;
import com.motionmate.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseScheduleRequest {

    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("exercise_id")
    private Long exerciseId;
    private String date;
    @JsonProperty("set_number")
    private Integer setNumber;
    private Double kg;
    private Integer reps;
    private String time;
}
