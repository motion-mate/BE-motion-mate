package com.motionmate.service.payment;

import com.motionmate.domain.goods.Order;
import com.motionmate.domain.goods.OrderRepository;
import com.motionmate.domain.payment.Payment;
import com.motionmate.domain.payment.PaymentRepository;
import com.motionmate.dto.payment.PaymentRequestDto;
import com.motionmate.dto.payment.PaymentResponseDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.global.exception.GlobalExceptionHandler;
import com.motionmate.mapper.payment.PaymentMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponseDto verifyAndSavePayment(PaymentRequestDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

        validatePaymentRequest(order, dto);

        Payment payment = PaymentMapper.toEntity(dto.getPaymentKey(), dto.getAmount(), order);
        order.updateStatus(Order.OrderStatus.FINISH); // 먼저 상태 변경
        payment.markSuccess(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);
        log.info("결제 저장 완료 후 응답 반환");
        return PaymentMapper.toDto(saved);
    }

    private void validatePaymentRequest(Order order, PaymentRequestDto dto) {
        if(order.getStatus() == Order.OrderStatus.FINISH) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 결제 완료된 주문입니다.");
        }
        if(dto.getAmount() <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제 금액이 유효하지 않습니다.");
        }
        if(order.getTotalAmount() != dto.getAmount()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제금액이 일치하지 않습니다.");
        }
        if(paymentRepository.existsByPaymentKey(dto.getPaymentKey())) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 처리된 결제입니다.");
        }
    }

    public void processWebhook(Map<String, Object> payload) {
        String paymentKey = (String) payload.get("paymentKey");
        Integer amount = (Integer) payload.get("amount");
        String status = (String) payload.get("status");

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제정보를 찾을 수 없습니다."));

        if ("SUCCESS".equalsIgnoreCase(status)) {
            payment.markSuccess(LocalDateTime.now());
        } else if ("FAIL".equalsIgnoreCase(status)) {
            payment.markFail();
        } else {
            payment.updateStatus(Payment.PaymentStatus.valueOf(status));
        }

        paymentRepository.save(payment);
    }
}
