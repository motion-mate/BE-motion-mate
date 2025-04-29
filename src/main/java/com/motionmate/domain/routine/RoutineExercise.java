package com.motionmate.domain.routine;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // 루틴은 필수
    private Routine routine;

    private String exerciseName; // 운동 이름 (ex: 벤치프레스)

    private Integer weight; // 무게 (kg)

    private Integer sets; // 세트 수

    private Integer reps; // 반복 수

    public RoutineExercise(Routine routine, String exerciseName, Integer weight, Integer sets, Integer reps) {
        this.routine = routine;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
    }
}
