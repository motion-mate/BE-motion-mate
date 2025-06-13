package com.motionmate.mapper.payment;

import com.motionmate.domain.goods.Order;
import com.motionmate.domain.payment.Payment;
import com.motionmate.dto.payment.PaymentResponseDto;

public class PaymentMapper {

    public static Payment toEntity(String paymentKey, int amount, Order order, Payment.PaymentStatus status) {
        return Payment.builder()
                .paymentKey(paymentKey)
                .amount(amount)
                .order(order)
                .status(status)
                .paidAt(null)
                .build();
    }

    public static PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .paymentKey(payment.getPaymentKey())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
