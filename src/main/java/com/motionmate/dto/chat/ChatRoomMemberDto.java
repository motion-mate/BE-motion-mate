package com.motionmate.dto.chat;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomMemberDto {

    private Long userId;
    private String nickname;
    private boolean connected;

}
