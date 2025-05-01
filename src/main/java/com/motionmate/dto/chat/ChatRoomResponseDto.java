package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoom.ExerciseType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class ChatRoomResponseDto {

    private Long id;
    private String title;
    private ExerciseType exerciseType;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDate promiseDate;
    private LocalTime promiseTime;
    private LocalDateTime promiseAt;
    private Long creatorId;
    private String creatorNickname;

    @Builder
    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.title = chatRoom.getTitle();
        this.exerciseType = chatRoom.getExerciseType();
        this.address = chatRoom.getAddress();
        this.latitude = chatRoom.getLatitude();
        this.longitude = chatRoom.getLongitude();
        this.createdAt = chatRoom.getCreatedAt();
        this.promiseDate = chatRoom.getPromiseDate();
        this.promiseTime = chatRoom.getPromiseTime();
        this.promiseAt = chatRoom.getPromiseAt();
        this.creatorId = chatRoom.getCreator().getId();
        this.creatorNickname = chatRoom.getCreator().getNickname();
    }
}
