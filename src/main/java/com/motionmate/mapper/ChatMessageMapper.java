package com.motionmate.mapper;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;

public class ChatMessageMapper {

    public static ChatMessage toEntity(ChatRoom room, User sender, ChatMessageRequestDto dto) {
        return new ChatMessage(room, sender, dto.getMessage(), dto.getType());
    }

    public static ChatMessageResponseDto toDto(ChatMessage message) {
        boolean connected;

        if (message.getType() == ChatMessage.MessageType.ENTER) {
            connected = true;
        } else if (message.getType() == ChatMessage.MessageType.QUIT) {
            connected = false;
        } else {
            connected = true; // TALK인 경우에도 연결 상태 유지
        }

        // 💡 sender나 profile이 null인 경우를 방어적으로 처리
        String nickname = "알 수 없음";
        if (message.getSender() != null && message.getSender().getProfile() != null) {
            nickname = message.getSender().getProfile().getNickname();
        }

        return ChatMessageResponseDto.builder()
                .id(message.getId())
                .senderNickname(nickname)
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .type(message.getType())
                .connected(connected)
                .build();
    }

}
