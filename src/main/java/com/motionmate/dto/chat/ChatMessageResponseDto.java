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
    private String senderProfileImageUrl;
    private String message;
    private LocalDateTime sentAt;
    private ChatMessage.MessageType type;
    private Boolean connected;

}
