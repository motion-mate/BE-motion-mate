package com.motionmate.domain.exercise;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseScheduleRepository extends JpaRepository<ExerciseSchedule, Long> {
    List<ExerciseSchedule> findByUser(User user);
}