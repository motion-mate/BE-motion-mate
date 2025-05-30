package com.motionmate.domain.routine;

import com.motionmate.domain.exercise.ExerciseList;
import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "routine")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 작성자 (User와 연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ 루틴 제목
    @Column(nullable = false)
    private String title;

    // 🔗 운동 정보 (Exercise와 연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseList exercise;

    // ✅ 운동 순서
    @Column(name = "set_number", nullable = false)
    private int setNumber;

    // ✅ 웨이트용
    private int kg;
    private int reps;

    // ✅ 유산소용
    private String time;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Routine(User user, String title, ExerciseList exercise,
                   int setNumber, int kg, int reps, String time, LocalDateTime createdAt) {
        this.user = user;
        this.title = title;
        this.exercise = exercise;
        this.setNumber = setNumber;
        this.kg = kg;
        this.reps = reps;
        this.time = time;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}