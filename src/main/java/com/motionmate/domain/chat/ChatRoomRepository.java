package com.motionmate.domain.chat;

import com.motionmate.domain.chat.ChatRoom.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 운동 종류, 주소(포함 검색), 날짜 기반 조건 필터링
    @Query("""
        SELECT r FROM ChatRoom r
        WHERE (:type IS NULL OR r.exerciseType = :type)
        AND (:address IS NULL OR LOWER(r.address) LIKE LOWER(CONCAT('%', :address, '%')))
        AND (:date IS NULL OR r.promiseDate = :date)
        AND (:keyword IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    List<ChatRoom> filterBy(
            @Param("type") ExerciseType type,
            @Param("address") String address,
            @Param("date") LocalDate date,
            @Param("keyword") String keyword
    );
}
