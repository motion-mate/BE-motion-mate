package com.motionmate.domain.chat;

import com.motionmate.domain.exercise.ExerciseRecord;
import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 채팅방 제목
    private String address; // 지도로 찍은게 아닌 생성자가 작성한 주소

    private Double latitude; // 위도
    private Double longitude; // 경도

    private LocalDateTime createdAt; // 생성일
    private LocalDateTime promiseAt; // 운동 예정일 및 시간

    private LocalDate promiseDate; // 운동 예정일
    private LocalTime promiseTime; // 운동 예정 시간

    @ManyToOne
    private User creator; // 방의 생성자

    @ManyToOne
    private ExerciseRecord exerciseType; // 운동 종류(수영, 러닝 등)

    public ChatRoom(String title, String address, Double latitude, Double longitude, LocalDate promiseDate, LocalTime promiseTime, User creator, ExerciseRecord exerciseType) {
        this.title = title;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now();
        this.promiseDate = promiseDate;
        this.promiseTime = promiseTime;
        this.promiseAt = LocalDateTime.of(promiseDate, promiseTime);
        this.creator = creator;
        this.exerciseType = exerciseType;
    }
}