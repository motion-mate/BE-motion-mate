package com.motionmate.domain.payment;

import com.motionmate.domain.goods.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // DB PK

    private String paymentKey;  // 포트원 결제 고유키 (String)

    private int amount;

    // Order와 양방향 매핑으로 연관관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    public enum PaymentStatus {
        SUCCESS, FAIL
    }

    @Builder
    public Payment(String paymentKey, int amount, Order order, PaymentStatus status, LocalDateTime paidAt) {
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.order = order;
        this.status = status;
        this.paidAt = paidAt;
    }

    // 상태 변경 메서드
    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

    public void markSuccess(LocalDateTime successTime) {
        this.status = PaymentStatus.SUCCESS;
        this.paidAt = successTime;
    }

    public void markFail() {
        this.status = PaymentStatus.FAIL;
    }
}
