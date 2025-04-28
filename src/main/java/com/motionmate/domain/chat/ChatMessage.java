package com.motionmate.domain.chat;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User sender;

    private String message;
    private LocalDateTime sentAt;

    public ChatMessage(ChatRoom chatRoom, User sender, String message) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.message = message;
        this.sentAt = LocalDateTime.now();
    }
}