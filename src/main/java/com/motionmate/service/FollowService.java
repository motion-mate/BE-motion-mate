package com.motionmate.service;

import com.motionmate.domain.follow.Follow;
import com.motionmate.domain.follow.FollowRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.follow.FollowCountResponse;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.follow.IsFollowingDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.FollowMapper;
import com.motionmate.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@AllArgsConstructor
@Service
@Getter
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    // fromUserId : 팔로우 하는 userId
    // toUserId : 팔로우 당하는 userId
    @Transactional
    public ResponseEntity<String> follow(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다.");
        }
        // 이미 팔로우한 유저인지 검증
        if(followRepository.existsByFromUser_IdAndToUser_Id(fromUserId, toUserId)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 팔로우한 유저입니다.");
        }
        // userRepository 에서 팔로우한 유저, 팔로우당한 유저 id 존재 여부 확인
        User fromUser = userRepository.findById(fromUserId)
                        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
        User toUser = userRepository.findById(toUserId)
                        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
        // 팔로우하는 userId와, 팔로우당하는 usrId를 파라미터로 받아서 팔로우관계 객체생성
        Follow follow = FollowMapper.toEntity(null, fromUser, toUser);
        followRepository.save(follow);

        fromUser.incrementFollowingCount();
        toUser.incrementFollowerCount();

        userRepository.save(fromUser);
        userRepository.save(toUser);

        return ResponseEntity.ok("팔로우 성공");
    }

    // 언팔로우
    @Transactional
    public void unfollow(Long fromUserId, Long toUserId) {
        // 팔로우관계 확인 -> 팔로우관계가 없을 시 예외처리
        Follow follow = followRepository.findByFromUser_IdAndToUser_Id(fromUserId, toUserId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "팔로우 관계가 존재하지 않습니다."));

        // 팔로우관계 삭제
        followRepository.delete(follow);

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        fromUser.decrementFollowingCount();
        toUser.decrementFollowerCount();

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    // 내가 팔로우한 유저 목록
    public List<FollowResponseDto> getFollowings(Long userId) {
        List<Follow> followings = followRepository.findAllByToUser_IdWithUser(userId);
        return followings.stream()
                .map(follow -> FollowMapper.toDto(follow.getToUser()))
                .toList();
    }

    // 나를 팔로우한 유저 목록
    public List<FollowResponseDto> getFollowers(Long userId) {
        List<Follow> followers = followRepository.findAllByToUser_IdWithUser(userId);
        return followers.stream()
                .map(follow -> FollowMapper.toDto(follow.getFromUser()))
                .toList();
    }

    // 팔로우 여부 확인
    public IsFollowingDto isFollowing(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        // 팔로우 관계가 존재하는지 확인
        boolean isFollowing = followRepository.existsByFromUser_IdAndToUser_Id(fromUserId, toUserId);

        // 팔로우 여부를 IsFollowingDto 에 담아서 반환
        return FollowMapper.toIsFollowingDto(isFollowing);
    }

    // 팔로우/팔로잉 수 반환
    public FollowCountResponse getFollowCounts(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."));

        return UserMapper.toFollowCountDto(user);
    }
}
