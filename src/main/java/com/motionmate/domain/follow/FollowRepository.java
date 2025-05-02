package com.motionmate.domain.follow;

import com.motionmate.domain.user.User;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.follow.IsFollowingDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Boolean existsByFromUser_IdAndToUser_Id(Long followerId, Long followerId1);

    Optional<Follow> findByFromUser_IdAndToUser_Id(Long fromUserId, Long toUserId);

    List<Follow> findAllByFromUser_Id(Long userId);

    List<Follow> findAllByToUser_Id(Long userId);
}
