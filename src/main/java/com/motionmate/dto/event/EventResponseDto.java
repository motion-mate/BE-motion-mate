package com.motionmate.dto.event;

import com.motionmate.domain.event.Event;
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
    private Event.EventType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private Integer stock;
}
