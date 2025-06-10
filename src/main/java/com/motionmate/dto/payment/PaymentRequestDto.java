package com.motionmate.dto.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestDto {

    private String paymentKey;
    private int amount;
    private Long orderId;
}
