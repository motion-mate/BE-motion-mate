package com.motionmate.controller.admin;

import com.motionmate.dto.event.EventRequestDto;
import com.motionmate.dto.event.EventResponseDto;
import com.motionmate.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final EventService eventService;

    // ✅ 전체 이벤트 목록 (관리자용)
    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEventsForAdmin() {
        List<EventResponseDto> events = eventService.getAllEvents(); // 필터 없이 전체 조회
        return ResponseEntity.ok(events);
    }

    // ✅ 단일 이벤트 조회
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getEvent(id);
        return ResponseEntity.ok(event);
    }

    // ✅ 이벤트 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long id, @RequestBody EventRequestDto dto) {
        eventService.updateEvent(id, dto);
        return ResponseEntity.ok().build();
    }

    // ✅ 숨김 처리 (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        eventService.deactivateEvent(id);
        return ResponseEntity.ok().build();
    }

    // 숨김 처리
    @PatchMapping("/{id}/hide")
    public ResponseEntity<Void> hideEvent(@PathVariable Long id) {
        eventService.hideEvent(id);
        return ResponseEntity.ok().build();
    }

    // 숨김 복구
    @PatchMapping("/{id}/unhide")
    public ResponseEntity<Void> unhideEvent(@PathVariable Long id) {
        eventService.unhideEvent(id);
        return ResponseEntity.ok().build();
    }
}
