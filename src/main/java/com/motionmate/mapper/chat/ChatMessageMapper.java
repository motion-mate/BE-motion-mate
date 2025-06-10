package com.motionmate.mapper.chat;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.mongo.ChatMessageDocument;

import java.time.LocalDateTime;

public class ChatMessageMapper {

    public static ChatMessage toEntity(ChatRoom room, User sender, ChatMessageRequestDto dto) {
        return new ChatMessage(room, sender, dto.getMessage(), dto.getType());
    }

    public static ChatMessageResponseDto toDto(ChatMessage message) {
        boolean connected = message.getType() == ChatMessage.MessageType.ENTER || message.getType() == ChatMessage.MessageType.TALK;

        String nickname = "알 수 없음";
        String profileImageUrl = null;

        if (message.getSender() != null && message.getSender().getProfile() != null) {
            nickname = message.getSender().getProfile().getNickname();
            profileImageUrl = message.getSender().getProfile().getProfileImageUrl();
        }

        return ChatMessageResponseDto.builder()
                .id(message.getId())
                .senderNickname(nickname)
                .senderProfileImageUrl(profileImageUrl)
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .type(message.getType())
                .connected(connected)
                .build();
    }

    public static ChatMessageResponseDto toDto(ChatMessageRequestDto dto) {
        boolean connected = dto.getType() != ChatMessage.MessageType.LEAVE
                && dto.getType() != ChatMessage.MessageType.EXIT;

        return ChatMessageResponseDto.builder()
                .id(null)
                .senderNickname(dto.getSenderNickname())
                .message(dto.getMessage())
                .sentAt(LocalDateTime.now())
                .type(dto.getType())
                .connected(connected)
                .build();
    }

    public static ChatMessageResponseDto fromDocument(ChatMessageDocument doc) {
        return ChatMessageResponseDto.builder()
                .senderNickname(doc.getSenderNickname())
                .message(doc.getMessage())
                .sentAt(doc.getSentAt())
                .type(ChatMessage.MessageType.valueOf(doc.getType()))
                .build();
    }

}
