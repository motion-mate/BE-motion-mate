package com.motionmate.domain.exercise;


import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "exercise_schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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