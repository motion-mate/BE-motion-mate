package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponseDto {

    private Long id;
    private String senderNickname;
    private String message;
    private LocalDateTime sentAt;

    public static ChatMessageResponseDto fromEntity(ChatMessage message) {
        return ChatMessageResponseDto.builder()
                .id(message.getId())
                .senderNickname(message.getSender().getNickname())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .build();
    }

}
