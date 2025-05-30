package com.motionmate.controller.event;

import com.motionmate.domain.user.User;
import com.motionmate.dto.event.EventRequestDto;
import com.motionmate.dto.event.EventResponseDto;
import com.motionmate.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    // ✅ 1. 이벤트 생성
    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody EventRequestDto dto) {
        eventService.createEvent(dto);
        return ResponseEntity.ok().build();
    }

    // ✅ 2. 이벤트 참여
    @PostMapping("/{eventId}/participate")
    public ResponseEntity<Void> participate(@PathVariable Long eventId,
                                            @AuthenticationPrincipal User user) {
        eventService.participate(user, eventId);
        return ResponseEntity.ok().build();
    }

    // ✅ 3. 전체 이벤트 조회
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // ✅ 4. 단일 이벤트 조회
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable Long eventId) {
        EventResponseDto event = eventService.getEvent(eventId);
        return ResponseEntity.ok(event);
    }

    // ✅ 5. 이벤트 수정
    @PutMapping("/{eventId}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long eventId,
                                            @RequestBody EventRequestDto dto) {
        eventService.updateEvent(eventId, dto);
        return ResponseEntity.ok().build();
    }

    // ✅ 6. 이벤트 숨김 처리 (삭제 아님)
    @PatchMapping("/{eventId}/deactivate")
    public ResponseEntity<Void> deactivateEvent(@PathVariable Long eventId) {
        eventService.deactivateEvent(eventId);
        return ResponseEntity.ok().build();
    }
}
