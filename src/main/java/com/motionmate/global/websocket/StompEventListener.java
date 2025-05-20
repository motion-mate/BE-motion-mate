package com.motionmate.global.websocket;

import com.motionmate.domain.chat.ChatRoomParticipantRepository;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class StompEventListener {

    private final ChatRoomParticipantRepository participantRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 연결 시
    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Object userAttr = accessor.getSessionAttributes().get("user");
        Object roomIdAttr = accessor.getSessionAttributes().get("roomId");

        if (userAttr instanceof User && roomIdAttr instanceof String) {
            User user = (User) userAttr;
            String roomId = (String) roomIdAttr;

            chatRoomRepository.findById(Long.parseLong(roomId)).ifPresent(room -> {
                participantRepository.findByChatRoomAndUser(room, user).ifPresent(participant -> {
                    participant.reconnect(); // 연결 상태 true 처리
                    participantRepository.save(participant);
                    System.out.println("✅ 접속 처리 완료: " + user.getProfile().getNickname());
                });
            });
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Object userAttr = accessor.getSessionAttributes().get("user");
        Object roomIdAttr = accessor.getSessionAttributes().get("roomId");

        if (userAttr instanceof User && roomIdAttr instanceof String) {
            User user = (User) userAttr;
            String roomId = (String) roomIdAttr;

            chatRoomRepository.findById(Long.parseLong(roomId)).ifPresent(room -> {
                participantRepository.findByChatRoomAndUser(room, user).ifPresent(participant -> {
                    participant.disconnect();
                    participantRepository.save(participant);
                    System.out.println("🚪 연결 끊김 처리 완료: " + user.getProfile().getNickname());
                });
            });
        }
    }

}