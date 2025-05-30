package com.motionmate.domain.exercise;


import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "exercise_schedule",
        uniqueConstraints = @UniqueConstraint(
                name = "uc_schedule_unique", // 제약조건 이름 (명시적으로 지정)
                columnNames = {"user_id", "date", "exercise_id", "set_number"}
        ))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
@Builder
public class ExerciseSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseList exercise;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "set_number", nullable = false)
    private int setNumber;

    @Column(name = "kg", nullable = true)
    private Double kg;

    @Column(name = "reps", nullable = true)
    private Integer reps;

    @Column(name = "time")
    private String time;




}