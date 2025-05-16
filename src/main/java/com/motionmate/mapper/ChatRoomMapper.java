package com.motionmate.mapper;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomParticipant;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatRoomMemberDto;
import com.motionmate.dto.chat.ChatRoomRequestDto;
import com.motionmate.dto.chat.ChatRoomResponseDto;

import java.util.List;

public class ChatRoomMapper {

    public static ChatRoom toEntity(ChatRoomRequestDto dto, User creator) {
        return new ChatRoom(
                dto.getTitle(),
                dto.getExerciseType(),
                dto.getRoadAddress(),
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
                .roadAddress(room.getRoadAddress())
                .address(room.getAddress())
                .latitude(room.getLatitude())
                .longitude(room.getLongitude())
                .createdAt(room.getCreatedAt())
                .promiseDate(room.getPromiseDate())
                .promiseTime(room.getPromiseTime())
                .promiseAt(room.getPromiseAt())
                .creatorId(room.getCreator().getId())
                .creatorNickname(room.getCreator().getProfile().getNickname())
                .members(List.of())
                .build();
    }


    public static ChatRoomResponseDto toDto(ChatRoom room, List<ChatRoomParticipant> participants) {

        List<ChatRoomMemberDto> members = participants.stream()
                .map(ChatRoomParticipantMapper::toDto)
                .toList();

        return ChatRoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .exerciseType(room.getExerciseType())
                .roadAddress(room.getRoadAddress())
                .address(room.getAddress())
                .latitude(room.getLatitude())
                .longitude(room.getLongitude())
                .createdAt(room.getCreatedAt())
                .promiseDate(room.getPromiseDate())
                .promiseTime(room.getPromiseTime())
                .promiseAt(room.getPromiseAt())
                .creatorId(room.getCreator().getId())
                .creatorNickname(room.getCreator().getProfile().getNickname())
                .members(members)
                .build();
    }

}
