package com.motionmate.service.delivery;

import com.motionmate.domain.delivery.Delivery;
import com.motionmate.domain.delivery.DeliveryRepository;
import com.motionmate.dto.delivery.DeliveryRequestDto;
import com.motionmate.dto.delivery.DeliveryResponseDto;
import com.motionmate.mapper.delivery.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryResponseDto createDelivery(DeliveryRequestDto dto) {
        Delivery delivery = DeliveryMapper.toEntity(dto);
        deliveryRepository.save(delivery);
        return DeliveryMapper.toDto(delivery);
    }

    public DeliveryResponseDto updateTracking(Long id, String courier, String trackingNumber) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("배송 정보가 존재하지 않습니다."));
        delivery.updateTracking(courier, trackingNumber);
        return DeliveryMapper.toDto(delivery);
    }

    public DeliveryResponseDto getDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("배송 정보가 존재하지 않습니다."));
        return DeliveryMapper.toDto(delivery);
    }
}
