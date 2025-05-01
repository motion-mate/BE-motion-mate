package com.motionmate.domain.exercise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "exerciseList")
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ExerciseCategory category;

    @OneToMany(mappedBy = "exercise", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseImage> imageUrl;

    public ExerciseList(String name, String description, ExerciseCategory category, List<ExerciseImage> imageUrl)
    {
        this.name=name;
        this.description=description;
        this.category=category;
        this.imageUrl=imageUrl;
    }


}
