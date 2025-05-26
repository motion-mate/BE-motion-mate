package com.motionmate.controller.delivery;

import com.motionmate.dto.delivery.DeliveryRequestDto;
import com.motionmate.dto.delivery.DeliveryResponseDto;
import com.motionmate.service.delivery.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // ✅ 배송 정보 등록 (테스트용 or 관리자)
    @PostMapping
    public ResponseEntity<DeliveryResponseDto> create(@RequestBody DeliveryRequestDto dto) {
        return ResponseEntity.ok(deliveryService.createDelivery(dto));
    }

    // ✅ 송장 정보 업데이트 (관리자)
    @PatchMapping("/{id}/tracking")
    public ResponseEntity<DeliveryResponseDto> updateTracking(
            @PathVariable Long id,
            @RequestBody DeliveryRequestDto dto) {
        return ResponseEntity.ok(deliveryService.updateTracking(id, dto.getCourier(), dto.getTrackingNumber()));
    }

    // ✅ 배송 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDelivery(id));
    }
}
