package com.motionmate.controller.goods;

import com.motionmate.domain.goods.EventStatus;
import com.motionmate.domain.goods.event.GoodsEvent;
import com.motionmate.dto.goods.event.GoodsEventRequestDto;
import com.motionmate.dto.goods.event.GoodsEventResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.mapper.goods.GoodsEventMapper;
import com.motionmate.service.goods.GoodsEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods/events")
public class GoodsEventController {

    private final GoodsEventService goodsEventService;

    // ✅ 1. 전체 이벤트 목록 조회
    @GetMapping
    public ResponseEntity<List<GoodsEventResponseDto>> getAllEvents(
            @RequestParam(required = false) EventStatus status) {

        List<GoodsEvent> events = (status == null)
                ? goodsEventService.getAllEvents()
                : goodsEventService.getEventsByStatus(status);

        List<GoodsEventResponseDto> result = events.stream()
                .map(event -> {
                    int remaining = goodsEventService.getRemainingStock(event.getId());
                    return GoodsEventMapper.toDto(event, remaining);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // ✅ 2. 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<GoodsEventResponseDto> getEvent(@PathVariable Long id) {
        GoodsEvent event = goodsEventService.getEventById(id);
        int remaining = goodsEventService.getRemainingStock(id);
        return ResponseEntity.ok(GoodsEventMapper.toDto(event, remaining));
    }

    // ✅ 3. 등록 (관리자 전용)
    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody @Valid GoodsEventRequestDto dto) {
        goodsEventService.createEvent(GoodsEventMapper.toEntity(dto));
        return ResponseEntity.ok().build();
    }

    // ✅ 4. 이벤트 참여 (선착순)
    @PostMapping("/{eventId}/participate")
    public ResponseEntity<Void> participateEvent(@AuthenticationPrincipal CustomOAuth2User user,
                                                 @PathVariable Long eventId) {
        goodsEventService.participate(user.getUser(), eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/remaining")
    public ResponseEntity<Integer> getRemainingStock(@PathVariable Long id) {
        int remaining = goodsEventService.getRemainingStock(id);
        return ResponseEntity.ok(remaining);
    }
}