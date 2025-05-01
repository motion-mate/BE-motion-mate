package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoom.ExerciseType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ChatRoomRequestDto {

    private String title;
    private ChatRoom.ExerciseType exerciseType;
    private String address;

    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    private LocalDate promiseDate;
    private LocalTime promiseTime;

    @NotNull
    private Long creatorId;
}
