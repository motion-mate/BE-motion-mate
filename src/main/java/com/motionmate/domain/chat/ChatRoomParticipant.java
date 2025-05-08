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

    private boolean connected; // 접속 여부 확인

    public ChatRoomParticipant(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.connected = true;
    }

    public void disconnect() {
        this.connected = false;
    }

    public void reconnect() {
        this.connected = true;
    }

}
