package com.motionmate.domain.exercise;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExerciseScheduleRepository extends JpaRepository<ExerciseSchedule, Long> {
    List<ExerciseSchedule> findByUser(User user);
    Optional<ExerciseSchedule> findByUser_IdAndDateAndExercise_NameAndExercise_CategoryAndSetNumber(
            Long userId, String date, String exerciseName, ExerciseCategory category, int setNumber
    );
    void deleteByUser_IdAndDateAndExercise_NameAndExercise_CategoryAndSetNumber(
            Long userId, String date, String name, ExerciseCategory category, int setNumber
    );
    @Query("SELECT COALESCE(MAX(e.setNumber), 0) FROM ExerciseSchedule e " +
            "WHERE e.user.id = :userId AND e.date = :date AND e.exercise.exerciseId = :exerciseId")
    int findMaxSetNumber(@Param("userId") Long userId,
                         @Param("date") String date,
                         @Param("exerciseId") Long exerciseId);

}