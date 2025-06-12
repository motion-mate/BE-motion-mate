package com.motionmate.mapper.event;

import com.motionmate.domain.event.Event;
import com.motionmate.dto.event.EventRequestDto;
import com.motionmate.dto.event.EventResponseDto;
import com.motionmate.dto.exercise.S3FileRequest;

public class EventMapper {

    public static Event toEntity(EventRequestDto dto, S3FileRequest imageInfo) {
        return Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .imageUrl(imageInfo != null ? imageInfo.url() : null)
                .orgName(imageInfo != null ? imageInfo.orgName() : null)           // ✅ 추가
                .bucketKey(imageInfo != null ? imageInfo.bucketKey() : null)     // ✅ 추가
                .type(dto.getType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .stock(dto.getStock())
                .hidden(false)                       // ✅ 명시적으로
                .manualDeactivated(dto.isManualDeactivated()) // ✅ 추가
                .build();
    }

    public static EventResponseDto toDto(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .orgName(event.getOrgName())
                .bucketKey(event.getBucketKey())
                .type(event.getType())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .active(event.isActive())
                .hidden(event.isHidden())            // ✅ 누락 보완
                .stock(event.getStock())
                .build();
    }
}
