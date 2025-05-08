package com.motionmate.domain.chat;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {

    Optional<ChatRoomParticipant> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    long countByChatRoomAndConnectedTrue(ChatRoom chatRoom);

}
