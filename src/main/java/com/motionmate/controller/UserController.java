package com.motionmate.controller;

import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.goods.cart.CartItemResponseDto;
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



    // 내 피드
    @GetMapping("/mypage/my-feeds")
    public List<FeedResponseDto> getMyFeeds(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMyFeeds(user.getUserId());
    }
//
//    @GetMapping("/mypage/my-records")
//    public List<ExerciseRecordDto> getMyRecords(@AuthenticationPrincipal CustomOAuth2User user) {
//        return userService.getMyRecords(user.getUserId());
//    }
//

    // 내 주문 목록
    @GetMapping("/mypage/my-orders")
    public List<OrderResponseDto> getMyOrders(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMyOrders(user.getUserId());
    }



    // 내가 팔로우하고 있는 사람들 (팔로잉)
    @GetMapping("/mypage/my-following")
    public List<FollowResponseDto> getMyFollowing(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMyFollowing(user.getUserId());
    }

    // 나를 팔로우하는 사람들 (팔로워)
    @GetMapping("/mypage/my-followers")
    public List<FollowResponseDto> getMyFollowers(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMyFollowers(user.getUserId());
    }

    // 나의 장바구니
    @GetMapping("/mypage/my-cart")
    public List<CartItemResponseDto> getMyCartItems(@AuthenticationPrincipal CustomOAuth2User user) {
        return userService.getMyCartItems(user.getUserId());
    }


}
