package com.motionmate.dto.event;

import com.motionmate.domain.event.Event;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EventRequestDto {
    private String title;
    private String description;
    private String imageUrl;
    private Event.EventType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer stock; // CONTEST는 null로 가능
}
