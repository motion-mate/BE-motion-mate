package com.motionmate.service;

import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfile;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.user.*;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.UserMapper;
import com.motionmate.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        profile.updateProfile(
                dto.getNickname(),
                dto.getBio(),
                dto.getGoal(),
                dto.getBirthDate(),
                dto.getProfileImageUrl()
        );
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
        profile.updateProfile(
                dto.getNickname(), // ✅ 닉네임도 수정 허용
                dto.getBio(),
                dto.getGoal(),
                dto.getBirthDate(),
                dto.getProfileImageUrl()
        );
    }


    // TODO 피드 목록
    /*
    @Transactional(readOnly = true)
    public List<FeedDto> getMyFeeds(Long userId) {
        // feedRepository.findByUserId(userId) 등
    }

    // TODO 운동 기록
    @Transactional(readOnly = true)
    public List<ExerciseRecordDto> getMyRecords(Long userId) {
        // exerciseRecordRepository.findByUserId(userId) 등
    }

    // TODO 주문 내역
    @Transactional(readOnly = true)
    public List<OrderDto> getMyOrders(Long userId) {
        // orderRepository.findByUserId(userId) 등
    }
    */
}