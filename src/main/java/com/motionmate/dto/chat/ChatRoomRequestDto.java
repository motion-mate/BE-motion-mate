package com.motionmate.dto.chat;

import com.motionmate.domain.chat.ChatRoom.ExerciseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ChatRoomRequestDto {

    @NotBlank(message = "채팅방 제목은 필수입니다.")
    private String title;

    @NotNull(message = "운동 종류를 선택해주세요.")
    private ExerciseType exerciseType;

    private String address; // 선택 입력

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    @NotNull(message = "운동 예정일을 입력해주세요.")
    private LocalDate promiseDate;

    @NotNull(message = "운동 예정 시간을 입력해주세요.")
    private LocalTime promiseTime;

    @NotNull
    private Long creatorId;

}
