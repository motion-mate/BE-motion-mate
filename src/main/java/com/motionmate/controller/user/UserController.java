package com.motionmate.controller.user;

import com.motionmate.dto.user.MainPageUserProfileDto;
import com.motionmate.dto.user.UserProfileDto;
import com.motionmate.dto.user.UserProfileRegisterRequestDto;
import com.motionmate.dto.user.UserProfileUpdateRequestDto;
import com.motionmate.dto.user.UserResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/mainprofile/{userId}")
    public ResponseEntity<MainPageUserProfileDto> getMainPageUserProfile(@PathVariable Long userId) {
        MainPageUserProfileDto userProfile = userService.getMainPageUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    // 최초 닉네임 등록 (회원가입 이후 첫 프로필 설정)
//    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
    @PatchMapping("/profile/register")
    public void registerProfile(@AuthenticationPrincipal CustomOAuth2User user,
                                @RequestBody UserProfileRegisterRequestDto dto) {
        userService.registerUser(user.getUserId(), dto);
    }


    // 내 정보 조회
    @GetMapping("/users/me")
    public UserProfileDto getMyInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        if (user == null) {
            log.error("❌ 인증된 사용자 없음 (SecurityContext에 없음)");
            throw new RuntimeException("로그인이 필요한 요청입니다.");
        }
//        return userService.getMyInfo(user.getUserId());
        log.info("🎯 getMyInfo 호출됨: {}", user.getUserId());
        UserProfileDto dto = userService.getMyInfo(user.getUserId());
        log.info("🎯 반환할 유저 프로필: {}", dto);
        return dto;
    }

    // 공개 유저 프로필 조회
    @GetMapping("/profile/{userId}")
    public UserProfileDto getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }

    // 내 프로필 수정
    @PatchMapping("/profile/edit")
    public void updateMyProfile(@AuthenticationPrincipal CustomOAuth2User user,
                                @RequestBody UserProfileUpdateRequestDto dto) {
        userService.updateUserProfile(user.getUserId(), dto);
    }

    @GetMapping("users/me/summary")
    public UserResponseDto getMySummary(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMySummary(user.getUserId());
    }

    @GetMapping("/mainprofile/me")
    public ResponseEntity<MainPageUserProfileDto> getMyMainProfile(@AuthenticationPrincipal CustomOAuth2User user) {
        Long userId = user.getUserId();
        MainPageUserProfileDto userProfile = userService.getMainPageUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/profile/me")
    public UserProfileDto getMyProfile(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getUserProfile(user.getUserId());
    }


}
