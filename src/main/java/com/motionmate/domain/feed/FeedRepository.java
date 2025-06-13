package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    //내 피드 조회용
    @Query("SELECT DISTINCT f FROM Feed f " +
            "JOIN FETCH f.user u " +
            "JOIN FETCH u.profile " +
            "WHERE u.id = :userId ORDER BY f.id DESC")
    List<Feed> findFeedsWithUserAndProfileByUserId(@Param("userId") Long userId);

    //커서 기반 페이징을 위한 피드 ID 목록 조회
    @Query("SELECT f.id FROM Feed f WHERE (:lastFeedId IS NULL OR f.id < :lastFeedId) ORDER BY f.id DESC")
    List<Long> findFeedIds(@Param("lastFeedId") Long lastFeedId, Pageable pageable);

    //N+1 방지를 위한 전체 피드 목록 조회용
    @Query("SELECT DISTINCT f FROM Feed f " +
            "JOIN FETCH f.user u " +
            "JOIN FETCH u.profile " +
            "WHERE f.id IN :ids")
    List<Feed> findFeedsWithUserAndProfile(@Param("ids") List<Long> ids);

    //삭제 권한 검증용 (작성자 ID만 추출)
    @Query("SELECT f.user.id FROM Feed f WHERE f.id = :id")
    Long findWriterIdByFeedId(@Param("id") Long id);


    //피드 상세조회용
    @Query("SELECT f FROM Feed f " +
            "JOIN FETCH f.user u " +
            "JOIN FETCH u.profile " +
            "LEFT JOIN FETCH f.images " +
            "WHERE f.id = :feedId")
    Optional<Feed> findFeedWithUserAndImage(@Param("feedId") Long feedId);





}