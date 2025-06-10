package com.motionmate.domain.chat;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class  ChatMessage {

    public enum MessageType {
        ENTER, TALK, EXIT, LEAVE
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private User sender; // 수신자

    private String message; // 메시지
    private LocalDateTime sentAt; // 보낸 시간

    @Enumerated(EnumType.STRING)
    private MessageType type;

    public ChatMessage(ChatRoom chatRoom, User sender, String message, MessageType type) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.message = message;
        this.sentAt = LocalDateTime.now();
        this.type = type;
    }
}