package com.motionmate.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileImageRepository extends JpaRepository<UserProfileImage, Long> {

    Optional<UserProfileImage> findByUserProfileId(Long userProfileId);

}
