package com.motionmate.dto.event;

import com.motionmate.domain.event.Event;
import com.motionmate.dto.exercise.S3FileRequest;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EventRequestDto {
    private String title;
    private String description;
    private S3FileRequest image; // ✅ 변경: imageUrl → image 객체로
    private String imageUrl;
    private String orgName;
    private String bucketKey;
    private Event.EventType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean manualDeactivated;
    private Integer stock; // CONTEST는 null로 가능
}
