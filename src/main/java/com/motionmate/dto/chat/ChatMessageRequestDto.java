package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // WebSocket을 사용할 때 JSON이 값을 바인딩 할 때 필요
@NoArgsConstructor
public class ChatMessageRequestDto {
    private Long chatRoomId; // 채팅방 ID
    private String message; // 메시지 내용
    private ChatMessage.MessageType type;
    private String senderNickname;
}
