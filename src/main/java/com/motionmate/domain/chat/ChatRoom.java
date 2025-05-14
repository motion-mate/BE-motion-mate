package com.motionmate.domain.chat;

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

    public enum ExerciseType {
        RUNNING, SWIMMING, CYCLING, GYM
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 채팅방 제목

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType; // 운동 종류

    @Column(nullable = false)
    private String roadAddress;

    private String address; // 지도로 찍은게 아닌 생성자가 작성한 주소

    @Column(nullable = false)
    private Double latitude; // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    private LocalDateTime createdAt; // 생성일

    private LocalDateTime promiseAt; // 운동 예정일 및 시간

    private LocalDate promiseDate; // 운동 예정일

    private LocalTime promiseTime; // 운동 예정 시간

    @ManyToOne
    private User creator; // 방의 생성자

    // 방장 위임
    public void setCreator(User user) { this.creator = user; }

    // 채팅방 제목 수정
    public void updateTitle(String title) {
        this.title = title;
    }

    // 운동 종류 수정
    public void updateExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    // 주소 수정
    public void updateAddress(String address) {
        this.address = address;
    }

    // 좌표 수정
    public void updateLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void updateLongitude(Double longitude) {
        this.longitude = longitude;
    }

    // 운동 날짜/시간 수정
    public void updatePromiseDate(LocalDate promiseDate) {
        this.promiseDate = promiseDate;
        this.promiseAt = LocalDateTime.of(promiseDate, this.promiseTime);
    }

    public void updatePromiseTime(LocalTime promiseTime) {
        this.promiseTime = promiseTime;
        this.promiseAt = LocalDateTime.of(this.promiseDate, promiseTime);
    }

    // 최종 생성자 (develop 브랜치 기준)
    public ChatRoom(String title, ExerciseType exerciseType, String roadAddress, String address,
                    Double latitude, Double longitude, LocalDate promiseDate, LocalTime promiseTime, User creator) {
        this.title = title;
        this.exerciseType = exerciseType;
        this.roadAddress = roadAddress;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now();
        this.promiseDate = promiseDate;
        this.promiseTime = promiseTime;
        this.promiseAt = LocalDateTime.of(promiseDate, promiseTime);
        this.creator = creator;
    }
}
