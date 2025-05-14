package com.motionmate.domain.chat;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User user;

    private boolean connected;  // 기본값 false

    public ChatRoomParticipant(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.connected = true;  // 새로 입장할 땐 true
    }

    public void reconnect() {
        this.connected = true;  // 재입장 시 true
    }

    public void disconnect() {
        this.connected = false;
    }
}

