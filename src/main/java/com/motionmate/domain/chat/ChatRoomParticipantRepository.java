package com.motionmate.domain.chat;

import com.motionmate.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {

    Optional<ChatRoomParticipant> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    List<ChatRoomParticipant> findByChatRoomAndConnectedTrue(ChatRoom chatRoom);

    List<ChatRoomParticipant> findByChatRoom(ChatRoom chatRoom); // 전체 멤버 조회

    long countByChatRoomAndConnectedTrue(ChatRoom chatRoom);

    long countByChatRoom(ChatRoom chatRoom);

    boolean existsByChatRoomAndUser(ChatRoom room, User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM ChatRoomParticipant p WHERE p.chatRoom.id = :chatRoomId")
    void deleteByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    List<ChatRoomParticipant> findByUser(User user);

}
