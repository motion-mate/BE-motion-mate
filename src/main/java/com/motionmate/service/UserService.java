package com.motionmate.service;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfile;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.user.*;
import com.motionmate.mapper.UserMapper;
import com.motionmate.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 내 정보 조회
    @Transactional(readOnly = true)
    public UserProfileDto getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        return UserProfileMapper.toUserProfileDto(user, profile);
    }

    // 다른 유저 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        return UserProfileMapper.toUserProfileDto(user, profile);
    }

    // 내 프로필 수정
    @Transactional
    public void updateUserProfile(Long userId, UserProfileUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        UserProfileMapper.updateFromDto(profile, dto);
    }


    // TODO 피드목록 완성되어야함
    /*// 내가 올린 피드 목록 조회
    @Transactional(readOnly = true)
    public List<FeedDto> getMyFeeds(Long userId) {
        // feedRepository.findByUserId(userId) 등
    }

    // TODO 기록한 운동 완성되어야함
    // 내가 기록한 운동 조회
    @Transactional(readOnly = true)
    public List<ExerciseRecordDto> getMyRecords(Long userId) {
        // exerciseRecordRepository.findByUserId(userId) 등
    }

    // TODO 주문내역 완성되어야함
    // 내가 주문한 굿즈 내역
    @Transactional(readOnly = true)
    public List<OrderDto> getMyOrders(Long userId) {
        // orderRepository.findByUserId(userId) 등
    }*/

}