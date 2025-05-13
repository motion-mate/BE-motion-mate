package com.motionmate.domain.exercise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "exerciseImage")
@Entity
public class ExerciseImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String bucketKey;
    @Column(nullable = false)
    private String orgName;

    @ManyToOne
    @JoinColumn(name = "exercise_Id", nullable = false)
    private ExerciseList exercise;

    public ExerciseImage(String url, String bucketKey, String orgName, ExerciseList exercise)
    {
        this.url = url;
        this.bucketKey = bucketKey;
        this.orgName = orgName;
        this.exercise = exercise;
    }
}
