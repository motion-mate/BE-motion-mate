package com.motionmate.mapper.chat;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomParticipant;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatRoomMemberDto;
import com.motionmate.dto.chat.ChatRoomRequestDto;
import com.motionmate.dto.chat.ChatRoomResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    // participants 정보 없이 기본 변환
    public static ChatRoomResponseDto toDto(ChatRoom room) {
        return toDto(room, Collections.emptyList());
    }

    // participants 정보가 있을 경우 포함한 변환
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
                .creatorId(Optional.ofNullable(room.getCreator()).map(User::getId).orElse(null))
                .creatorNickname(Optional.ofNullable(room.getCreator())
                        .map(User::getProfile)
                        .map(p -> p.getNickname())
                        .orElse("알 수 없음"))
                .members(members)
                .build();
    }
}
