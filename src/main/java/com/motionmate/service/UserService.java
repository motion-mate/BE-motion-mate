package com.motionmate.service;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedRepository;
import com.motionmate.domain.follow.Follow;
import com.motionmate.domain.follow.FollowRepository;
import com.motionmate.domain.goods.CartItem;
import com.motionmate.domain.goods.CartItemRepository;
import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfile;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.feed.FeedResponseDto;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.goods.cart.CartItemResponseDto;
import com.motionmate.dto.goods.order.OrderResponseDto;
import com.motionmate.dto.user.*;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.FeedMapper;
import com.motionmate.mapper.FollowMapper;
import com.motionmate.mapper.UserMapper;
import com.motionmate.mapper.UserProfileMapper;
import com.motionmate.mapper.goods.CartItemMapper;
import com.motionmate.mapper.goods.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public void registerUser(Long userId, UserProfileRegisterRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();

        if (profile.getNickname() != null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 닉네임이 설정되어 있습니다.");
        }

        UserProfileMapper.updateFromDto(profile, dto);

    }



    // 내 정보 조회
    @Transactional(readOnly = true)
    public UserProfileDto getMyInfo(Long userId) {
        if (userId == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        return UserProfileMapper.toUserProfileDto(user, profile);
    }

    // 다른 유저 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        return UserProfileMapper.toUserProfileDto(user, profile);
    }

    // 내 프로필 수정
    @Transactional
    public void updateUserProfile(Long userId, UserProfileUpdateRequestDto dto) {
        if (userId == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();

        // profile == null 은 일반적으로 발생하지 않지만 방어 코드로 남겨둠
        if (profile == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "아직 프로필이 생성되지 않았습니다.");
        }

        // 닉네임은 여기서도 수정 가능 (인스타그램처럼 바꾸는 구조 허용)
        UserProfileMapper.updateFromDto(profile, dto);

    }


//    // TODO 피드 목록
//    @Transactional(readOnly = true)
//    public List<FeedResponseDto> getMyFeeds(Long userId) {
//        List<Feed> feeds = feedRepository.findByUserId(userId);
//
//        return feeds.stream()
//                .map(FeedMapper::fromEntity)
//                .toList();
//    }


//    // TODO 운동 기록
//    @Transactional(readOnly = true)
//    public List<ExerciseRecordDto> getMyRecords(Long userId) {
//        // exerciseRecordRepository.findByUserId(userId) 등
//    }

//    // TODO 주문 내역
//    @Transactional(readOnly = true)
//    public List<OrderResponseDto> getMyOrders(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
//
//        List<Order> orders = orderRepository.findByUser(user);
//
//        return orders.stream()
//                .map(OrderMapper::toDto)
//                .toList();
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<FollowResponseDto> getMyFollowing(Long userId) {
//        List<Follow> followings = followRepository.findAllByFromUser_Id(userId);
//        return followings.stream()
//                .map(f -> FollowMapper.toDto(f.getToUser()))
//                .toList();
//    }
//
//    @Transactional(readOnly = true)
//    public List<FollowResponseDto> getMyFollowers(Long userId) {
//        List<Follow> followers = followRepository.findAllByToUser_Id(userId);
//        return followers.stream()
//                .map(f -> FollowMapper.toDto(f.getFromUser()))
//                .toList();
//    }
//
//    public List<CartItemResponseDto> getMyCartItems(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
//
//        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);
//        return cartItems.stream()
//                .map(CartItemMapper::toDto)
//                .toList();
//    }




}