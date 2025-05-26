package com.motionmate.dto.delivery;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryResponseDto {
    private Long id;
    private String recipientName;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String courier;
    private String trackingNumber;
}
