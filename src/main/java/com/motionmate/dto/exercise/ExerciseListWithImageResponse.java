package com.motionmate.dto.exercise;

import com.motionmate.domain.exercise.ExerciseCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseListWithImageResponse {
    private Long exerciseId;
    private String name;
    private String description;
    private ExerciseCategory category;
    private SaveImageResponse image;

}
