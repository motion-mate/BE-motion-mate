package com.motionmate.domain.delivery;

import com.motionmate.domain.goods.Order;
import com.motionmate.domain.event.EventParticipation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class    Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipientName;
    private String phoneNumber;
    private String address;
    private String zipcode;

    private String courier;         // 택배사
    private String trackingNumber;  // 송장번호

    public void updateTracking(String courier, String trackingNumber) {
        this.courier = courier;
        this.trackingNumber = trackingNumber;
    }
}