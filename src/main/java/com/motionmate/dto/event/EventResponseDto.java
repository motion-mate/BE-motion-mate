package com.motionmate.dto.event;

import com.motionmate.domain.event.Event;
import com.motionmate.dto.exercise.S3FileRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String orgName;
    private String bucketKey;    private Event.EventType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private boolean manualDeactivated;
    private boolean hidden;
    private Integer stock;
}
