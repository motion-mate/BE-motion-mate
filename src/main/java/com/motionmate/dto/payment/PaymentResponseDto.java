package com.motionmate.dto.payment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponseDto {

    private Long paymentId;
    private String paymentKey;
    private int amount;
    private String status;
    private LocalDateTime paidAt;
    private Long orderId;
}
