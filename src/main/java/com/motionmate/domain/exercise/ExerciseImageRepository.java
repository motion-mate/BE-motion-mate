package com.motionmate.domain.exercise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseImageRepository extends JpaRepository<ExerciseImage,Long> {
    Optional<ExerciseImage> findFirstByExercise_ExerciseId(Long exerciseId);
}
