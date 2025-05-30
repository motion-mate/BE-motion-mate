package com.motionmate.domain.exercise;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseListRepository extends JpaRepository<ExerciseList, Long> {
    Optional<ExerciseList> findByNameAndCategory(String name, ExerciseCategory category);
}
