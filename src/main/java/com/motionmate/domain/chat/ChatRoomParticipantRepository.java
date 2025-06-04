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

    List<ChatRoomParticipant> findByChatRoom(ChatRoom chatRoom); // 전체 멤버 조회

    boolean existsByChatRoomAndUser(ChatRoom room, User user);

    @Query("SELECT crp FROM ChatRoomParticipant crp " +
            "JOIN FETCH crp.user " +
            "JOIN FETCH crp.chatRoom " +
            "WHERE crp.chatRoom = :chatRoom AND crp.user = :user")
    Optional<ChatRoomParticipant> findByChatRoomAndUserWithAll(
            @Param("chatRoom") ChatRoom chatRoom,
            @Param("user") User user);


    @Modifying
    @Transactional
    @Query("DELETE FROM ChatRoomParticipant p WHERE p.chatRoom.id = :chatRoomId")
    void deleteByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    List<ChatRoomParticipant> findByUser(User user);

    Optional<ChatRoomParticipant> findByChatRoom_idAndUser_profile_nickname(Long roomId, String nickname);

    @Query("SELECT crp FROM ChatRoomParticipant crp " +
            "JOIN FETCH crp.chatRoom cr " +
            "JOIN FETCH cr.creator c " +
            "JOIN FETCH c.profile " +
            "WHERE crp.user = :user")
    List<ChatRoomParticipant> findByUserWithChatRoomAndCreator(@Param("user") User user);

    @Query("SELECT crp FROM ChatRoomParticipant crp " +
            "JOIN FETCH crp.user u " +
            "JOIN FETCH u.profile " +
            "WHERE crp.chatRoom = :room")
    List<ChatRoomParticipant> findByChatRoomWithUserProfile(@Param("room") ChatRoom room);


}
