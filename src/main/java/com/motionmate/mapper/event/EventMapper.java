package com.motionmate.mapper.event;

import com.motionmate.domain.event.Event;
import com.motionmate.dto.event.EventRequestDto;
import com.motionmate.dto.event.EventResponseDto;

public class EventMapper {

    public static Event toEntity(EventRequestDto dto) {
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .type(dto.getType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .stock(dto.getStock())
                .build();
    }

    public static EventResponseDto toDto(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .type(event.getType())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .active(event.isActive())
                .stock(event.getStock())
                .build();
    }
}
