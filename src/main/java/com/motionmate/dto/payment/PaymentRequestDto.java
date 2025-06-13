package com.motionmate.dto.payment;

import com.motionmate.domain.payment.Payment;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentRequestDto {

    private String paymentKey;
    private int amount;
    private Long orderId;
    private Payment.PaymentStatus paymentStatus;
}
