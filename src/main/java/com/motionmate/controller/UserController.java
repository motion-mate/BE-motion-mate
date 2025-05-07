package com.motionmate.controller;

import com.motionmate.dto.goods.order.OrderResponseDto;
import com.motionmate.dto.user.UserProfileDto;
import com.motionmate.dto.user.UserProfileRegisterRequestDto;
import com.motionmate.dto.user.UserProfileUpdateRequestDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // 최초 닉네임 등록 (회원가입 이후 첫 프로필 설정)
    @PatchMapping("/profile/register")
    public void registerProfile(@AuthenticationPrincipal CustomOAuth2User user,
                                @RequestBody UserProfileRegisterRequestDto dto) {
        userService.registerUser(user.getUserId(), dto);
    }


    // 내 정보 조회
    @GetMapping("/users/me")
    public UserProfileDto getMyInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMyInfo(user.getUserId());
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



//    @GetMapping("/mypage/my-feeds")
//    public List<FeedDto> getMyFeeds(@AuthenticationPrincipal CustomOAuth2User user) {
//        return userService.getMyFeeds(user.getUserId());
//    }
//
//    @GetMapping("/mypage/my-records")
//    public List<ExerciseRecordDto> getMyRecords(@AuthenticationPrincipal CustomOAuth2User user) {
//        return userService.getMyRecords(user.getUserId());
//    }
//
    @GetMapping("/mypage/my-orders")
    public List<OrderResponseDto> getMyOrders(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMyOrders(user.getUserId());
    }





}
