package com.motionmate.dto.payment;

import com.motionmate.domain.payment.Payment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponseDto {

    private Long paymentId;
    private String paymentKey;
    private int amount;
    private Payment.PaymentStatus paymentStatus;
    private LocalDateTime paidAt;
}
