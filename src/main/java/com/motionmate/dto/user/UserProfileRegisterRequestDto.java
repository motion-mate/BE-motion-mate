package com.motionmate.dto.user;

import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Getter
public class UserProfileRegisterRequestDto {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    private String bio;
    private String goal;
    private LocalDate birthDate;
    private String profileImageUrl;
}
