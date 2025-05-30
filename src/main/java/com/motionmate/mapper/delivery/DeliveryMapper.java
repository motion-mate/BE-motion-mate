package com.motionmate.mapper.delivery;

import com.motionmate.domain.delivery.Delivery;
import com.motionmate.dto.delivery.DeliveryRequestDto;
import com.motionmate.dto.delivery.DeliveryResponseDto;

public class DeliveryMapper {

    public static Delivery toEntity(DeliveryRequestDto dto) {
        return Delivery.builder()
                .recipientName(dto.getRecipientName())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .zipcode(dto.getZipcode())
                .courier(dto.getCourier())
                .trackingNumber(dto.getTrackingNumber())
                .build();
    }

    public static DeliveryResponseDto toDto(Delivery delivery) {
        return DeliveryResponseDto.builder()
                .id(delivery.getId())  // ✅ 있으면 좋음
                .recipientName(delivery.getRecipientName())
                .phoneNumber(delivery.getPhoneNumber())
                .address(delivery.getAddress())
                .zipcode(delivery.getZipcode())
                .courier(delivery.getCourier())
                .trackingNumber(delivery.getTrackingNumber())
                .build();
    }
}
