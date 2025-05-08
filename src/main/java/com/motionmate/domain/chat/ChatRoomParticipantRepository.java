package com.motionmate.domain.chat;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {

    Optional<ChatRoomParticipant> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    List<ChatRoomParticipant> findByChatRoomAndConnectedTrue(ChatRoom chatRoom);

    List<ChatRoomParticipant> findByChatRoom(ChatRoom chatRoom); // 전체 멤버 조회

    long countByChatRoomAndConnectedTrue(ChatRoom chatRoom);

}
