package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatMessage;
import lombok.*;

@ToString
@Getter
@Setter // WebSocket을 사용할 때 JSON이 값을 바인딩 할 때 필요
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatMessageRequestDto {
    private Long chatRoomId; // 채팅방 ID
    private String message; // 메시지 내용
    private ChatMessage.MessageType type;
    private String senderNickname;
}
