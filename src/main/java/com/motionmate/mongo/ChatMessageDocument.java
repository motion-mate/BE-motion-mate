package com.motionmate.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessageDocument {

    @Id
    private String id;
    private Long roomId;
    private Long senderId;
    private String senderNickname;
    private String message;
    private LocalDateTime sentAt;
    private String type; // ENTER, TALK, QUIT

    @Builder
    public ChatMessageDocument(Long roomId, Long senderId, String senderNickname, String message, LocalDateTime sentAt, String type) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.message = message;
        this.sentAt = sentAt;
        this.type = type;
    }
}
