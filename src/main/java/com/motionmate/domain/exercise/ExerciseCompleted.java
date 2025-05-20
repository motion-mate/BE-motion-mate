package com.motionmate.domain.exercise;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_completed" ,
        uniqueConstraints = @UniqueConstraint(
      columnNames = {"user_id", "date", "exercise_category", "exercise_name", "set_number"}
)
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseCompleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private String exerciseName;

    private String exerciseCategory;

    private int setNumber;

    private int kg;

    private int reps;

    private String time;

    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
