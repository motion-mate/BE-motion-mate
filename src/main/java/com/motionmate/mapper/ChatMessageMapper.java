package com.motionmate.mapper;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;

public class ChatMessageMapper {

    public static ChatMessage toEntity(ChatRoom room, User sender, ChatMessageRequestDto dto) {
        return new ChatMessage(room, sender, dto.getMessage());
    }

    public static ChatMessageResponseDto toDto(ChatMessage message) {
        return ChatMessageResponseDto.builder()
                .id(message.getId())
                .senderNickname(message.getSender().getNickname())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .build();
    }

}
