package com.motionmate.mapper;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatRoomRequestDto;
import com.motionmate.dto.chat.ChatRoomResponseDto;

public class ChatRoomMapper {

    public static ChatRoom toEntity(ChatRoomRequestDto dto, User creator) {
        return new ChatRoom(
                dto.getTitle(),
                dto.getExerciseType(),
                dto.getAddress(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getPromiseDate(),
                dto.getPromiseTime(),
                creator
        );
    }

    public static ChatRoomResponseDto toDto(ChatRoom room) {
        return ChatRoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .exerciseType(room.getExerciseType())
                .address(room.getAddress())
                .latitude(room.getLatitude())
                .longitude(room.getLongitude())
                .createdAt(room.getCreatedAt())
                .promiseDate(room.getPromiseDate())
                .promiseTime(room.getPromiseTime())
                .promiseAt(room.getPromiseAt())
                .creatorId(room.getCreator().getId())
                .creatorNickname(room.getCreator().getNickname())
                .build();
    }

}
