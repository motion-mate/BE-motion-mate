package com.motionmate.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.profile " +
            "LEFT JOIN FETCH u.followers f1 " +
            "LEFT JOIN FETCH f1.fromUser " +
            "LEFT JOIN FETCH u.followings f2 " +
            "LEFT JOIN FETCH f2.toUser " +
            "WHERE u.id = :id")
    Optional<User> findWithProfileAndFollowById(@Param("id") Long id);
}