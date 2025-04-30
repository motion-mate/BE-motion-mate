package com.motionmate.dto.user;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserProfileUpdateRequestDto {
    private String bio;
    private String goal;
    private LocalDate birthDate;
}