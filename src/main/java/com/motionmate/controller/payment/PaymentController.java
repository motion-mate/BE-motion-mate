package com.motionmate.controller.payment;

import com.motionmate.domain.goods.Order;
import com.motionmate.domain.payment.Payment;
import com.motionmate.dto.payment.PaymentRequestDto;
import com.motionmate.dto.payment.PaymentResponseDto;
import com.motionmate.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDto> verifyPayment(@RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto responseDto = paymentService.verifyAndSavePayment(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
