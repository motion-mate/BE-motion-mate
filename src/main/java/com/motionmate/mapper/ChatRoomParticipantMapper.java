package com.motionmate.mapper;

import com.motionmate.domain.chat.ChatRoomParticipant;
import com.motionmate.dto.chat.ChatRoomMemberDto;

public class ChatRoomParticipantMapper {

    public static ChatRoomMemberDto toDto(ChatRoomParticipant participant) {
        return ChatRoomMemberDto.builder()
                .userId(participant.getUser().getId())
                .nickname(participant.getUser().getProfile().getNickname())
                .connected(participant.isConnected())
                .build();
    }

}
