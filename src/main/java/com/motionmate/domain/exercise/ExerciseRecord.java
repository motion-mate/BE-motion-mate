package com.motionmate.domain.exercise;

import com.motionmate.domain.routine.Routine;
import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne(optional = true) // 루틴이 없을 수도 있으므로 nullable 허용
    private Routine routine;

    private String type; // "헬스", "러닝", "수영"
    private Integer setCount;
    private Integer weight;
    private Integer reps;
    private Double distance;
    private Integer duration;
    private LocalDate exerciseDate;

    public ExerciseRecord(User user, Routine routine, String type, Integer setCount, Integer weight, Integer reps, Double distance, Integer duration, LocalDate exerciseDate) {
        this.user = user;
        this.routine = routine;
        this.type = type;
        this.setCount = setCount;
        this.weight = weight;
        this.reps = reps;
        this.distance = distance;
        this.duration = duration;
        this.exerciseDate = exerciseDate;
    }
}