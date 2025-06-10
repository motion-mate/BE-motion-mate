package com.motionmate.domain.follow;

import com.motionmate.domain.user.User;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.follow.IsFollowingDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Boolean existsByFromUser_IdAndToUser_Id(Long followerId, Long followerId1);

    Optional<Follow> findByFromUser_IdAndToUser_Id(Long fromUserId, Long toUserId);

    List<Follow> findAllByFromUser_Id(Long userId);

    List<Follow> findAllByToUser_Id(Long userId);

    @Query("SELECT f FROM Follow f JOIN FETCH f.toUser WHERE f.fromUser.id = :userId")
    List<Follow> findAllByFromUser_IdWithUser(@Param("userId") Long userId);

    @Query("SELECT f FROM Follow f JOIN FETCH f.fromUser WHERE f.toUser.id = :userId")
    List<Follow> findAllByToUser_IdWithUser(@Param("userId") Long userId);

    boolean existsByFromUserAndToUser(User user, User user1);

    @Query("SELECT f.toUser.id FROM Follow f WHERE f.fromUser.id = :userId")
    List<Long> findFollowing(@Param("userId") Long userId);
}
