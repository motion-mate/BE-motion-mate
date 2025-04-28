package com.motionmate.domain.exercise;

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

    private String type; // "헬스", "러닝", "수영"

    private Integer setCount;
    private Integer weight;
    private Integer reps;

    private Double distance;
    private Integer duration;

    private LocalDate exerciseDate;

    public ExerciseRecord(User user, String type, Integer setCount, Integer weight, Integer reps, Double distance, Integer duration, LocalDate exerciseDate) {
        this.user = user;
        this.type = type;
        this.setCount = setCount;
        this.weight = weight;
        this.reps = reps;
        this.distance = distance;
        this.duration = duration;
        this.exerciseDate = exerciseDate;
    }
}