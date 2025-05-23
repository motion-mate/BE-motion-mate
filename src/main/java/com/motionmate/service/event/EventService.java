package com.motionmate.service.event;

import com.motionmate.domain.event.Event;
import com.motionmate.domain.event.EventParticipation;
import com.motionmate.domain.event.EventParticipationRepository;
import com.motionmate.domain.event.EventRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.event.EventRequestDto;
import com.motionmate.dto.event.EventResponseDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.event.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventParticipationRepository participationRepository;

    // ✅ 1. 이벤트 생성
    public Event createEvent(EventRequestDto dto) {
        Event event = EventMapper.toEntity(dto);
        return eventRepository.save(event);
    }

    // ✅ 2. 이벤트 참여
    public void participate(User user, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));

        if (!event.isActive()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이벤트 비활성화됨");
        }

        if (event.getType() == Event.EventType.GIVEAWAY) {
            event.decreaseStock();
        }

        participationRepository.save(new EventParticipation(user, event));

        if (event.getType() == Event.EventType.GIVEAWAY && event.getStock() == 0) {
            event.deactivate();
        }
    }

    // ✅ 3. 전체 이벤트 조회
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventMapper::toDto)
                .toList();
    }

    // ✅ 4. 단일 이벤트 조회
    @Transactional(readOnly = true)
    public EventResponseDto getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));
        return EventMapper.toDto(event);
    }

    // ✅ 5. 이벤트 수정
    public void updateEvent(Long id, EventRequestDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));

        event.update(
                dto.getTitle(),
                dto.getDescription(),
                dto.getImageUrl(),
                dto.getType(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getStock()
        );
    }

    // ✅ 6. 이벤트 숨김 처리 (soft delete)
    public void deactivateEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));
        event.deactivate(); // active = false
    }
}
