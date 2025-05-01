package com.motionmate.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    // ✅ 닉네임으로 조회 (중복 방지 등용)
    Optional<UserProfile> findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.profile.nickname = :nickname")
    Optional<User> findUserByNickname(@Param("nickname") String nickname);

    // (선택) 닉네임 존재 여부 확인
    boolean existsByNickname(String nickname);
}
