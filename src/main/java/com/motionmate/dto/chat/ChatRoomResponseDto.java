package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoom.ExerciseType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
public class ChatRoomResponseDto {

    private Long id;
    private String title;
    private ExerciseType exerciseType;
    private String roadAddress;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDate promiseDate;
    private LocalTime promiseTime;
    private LocalDateTime promiseAt;
    private Long creatorId;
    private String creatorNickname;
    private List<ChatRoomMemberDto> members;

}
