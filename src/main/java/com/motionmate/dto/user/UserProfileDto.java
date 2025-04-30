package com.motionmate.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;

    private String bio;
    private String goal;
    private LocalDate birthDate;


}