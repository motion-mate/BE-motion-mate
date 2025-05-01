package com.motionmate.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private String bio;
    private String goal;
    private LocalDate birthDate;

    @Builder
    public UserProfile(String bio, String goal, LocalDate birthDate) {
        this.bio = bio;
        this.goal = goal;
        this.birthDate = birthDate;
    }


    public void updateProfile(String bio, String goal, LocalDate birthDate) {
        this.bio = bio;
        this.goal = goal;
        this.birthDate = birthDate;
    }
}
