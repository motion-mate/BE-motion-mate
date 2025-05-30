package com.motionmate.domain.routine;

import com.motionmate.domain.exercise.ExerciseCategory;
import com.motionmate.domain.exercise.ExerciseList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUserId(Long userId);
    List<Routine> findByUser_IdAndTitle(Long userId, String title); // 특정 루틴 이름 가져올 때
    void deleteByUserIdAndTitle(Long userId, String title);
    void deleteByUserIdAndTitleAndExercise_NameAndExercise_CategoryAndSetNumber(
            Long userId,
            String title,
            String name,
            ExerciseCategory exerciseCategory,
            int setNumber
    );
    List<Routine> findByUserIdAndTitle(Long userId, String title);

}