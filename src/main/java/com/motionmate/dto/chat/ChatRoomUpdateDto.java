package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatRoom;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ChatRoomUpdateDto {

    private String title;
    private ChatRoom.ExerciseType exerciseType;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDate promiseDate;
    private LocalTime promiseTime;

}
