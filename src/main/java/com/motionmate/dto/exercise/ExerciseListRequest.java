package com.motionmate.dto.exercise;

import com.motionmate.domain.exercise.ExerciseCategory;

public record ExerciseListRequest(
        String name,
        String description,
        ExerciseCategory category,
        S3FileRequest imageUrl
) {
}
