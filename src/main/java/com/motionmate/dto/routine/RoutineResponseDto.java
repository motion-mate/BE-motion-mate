package com.motionmate.dto.routine;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.motionmate.domain.exercise.ExerciseCategory;
import com.motionmate.domain.routine.Routine;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class RoutineResponseDto {
    private Long id;
    @JsonProperty("exercise_name")
    private final String exerciseName;

    @JsonProperty("exercise_category")
    private final ExerciseCategory exerciseCategory;

    @JsonProperty("set_number")
    private final int setNumber;

    private final int kg;
    private final int reps;
    private final String time;
    private Long exerciseId;

    public RoutineResponseDto(Routine routine) {
        this.id = routine.getId();
        this.exerciseName = routine.getExercise().getName();
        this.exerciseCategory = routine.getExercise().getCategory();
        this.setNumber = routine.getSetNumber();
        this.kg = routine.getKg();
        this.reps = routine.getReps();
        this.time = routine.getTime();
        this.exerciseId = routine.getExercise().getExerciseId();
    }
}