package com.motionmate.dto.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRequestDto {
    private String recipientName;
    private String phoneNumber;
    private String address;
    private String zipcode;
    private String courier;
    private String trackingNumber;
}