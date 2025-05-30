package com.motionmate.dto.routine;

import com.motionmate.domain.exercise.ExerciseCategory;
import com.motionmate.domain.exercise.ExerciseList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineUpdateRequest {
    private Long userId;
    private String title;
    private List<ExerciseDto> exercises;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseDto {
        private String exerciseName;
        private ExerciseCategory exerciseCategory;
        private int setNumber;
        private Integer kg;
        private Integer reps;
        private String time;
    }
}
