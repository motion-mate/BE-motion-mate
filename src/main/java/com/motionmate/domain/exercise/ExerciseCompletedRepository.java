package com.motionmate.domain.exercise;


import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExerciseCompletedRepository extends JpaRepository<ExerciseCompleted, Long> {
    List<ExerciseCompleted> findByUserId(Long userId);
    void deleteByUserIdAndDateAndExerciseNameAndExerciseCategoryAndSetNumber(Long userId, String date, String exerciseName, String exerciseCategory, int setNumber);
    Optional<ExerciseCompleted> findByUserAndDateAndExerciseCategoryAndExerciseNameAndSetNumber(
            User user,
            String date,
            String exerciseCategory,
            String exerciseName,
            int setNumber);
}
