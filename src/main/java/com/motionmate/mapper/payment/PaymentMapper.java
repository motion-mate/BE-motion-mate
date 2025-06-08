package com.motionmate.mapper.payment;

import com.motionmate.domain.goods.Order;
import com.motionmate.domain.payment.Payment;
import com.motionmate.dto.payment.PaymentResponseDto;

public class PaymentMapper {

    public static Payment toEntity(String paymentKey, int amount, Order order) {
        return Payment.builder()
                .paymentKey(paymentKey)
                .amount(amount)
                .status(Payment.PaymentStatus.SUCCESS)
                .paidAt(null)
                .order(order)
                .build();
    }

    public static PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .paymentKey(payment.getPaymentKey())
                .amount(payment.getAmount())
                .status(payment.getStatus().name())
                .paidAt(payment.getPaidAt())
                .orderId(payment.getOrder().getId())
                .build();
    }
}
