package com.motionmate.domain.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByPaymentKey(String paymentId);

    Optional<Payment> findByPaymentKey(String paymentKey);
}
