package com.motionmate.dto.exercise;

import com.motionmate.domain.exercise.ExerciseCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExerciseListWithImageResponse {
    private Long exerciseId;
    private String name;
    private ExerciseCategory category;
    private SaveImageResponse image;

    public ExerciseListWithImageResponse(Long exerciseId, String name, ExerciseCategory category, SaveImageResponse image)
    {
        this.exerciseId=exerciseId;
        this.name=name;
        this.category=category;
        this.image=image;
    }
}
