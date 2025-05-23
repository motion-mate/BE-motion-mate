package com.motionmate.domain.goods;

import com.motionmate.domain.delivery.Delivery;
import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    public enum OrderStatus {
        READY, GOING, FINISH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @Enumerated(EnumType.STRING) // ✅ Enum은 문자열로 저장
    private OrderStatus status;

    private LocalDateTime orderedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 연관관계 세팅용 (Order → OrderItem)
     */
    public void applyOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        for (OrderItem item : orderItems) {
            item.setOrder(this);
        }
    }

    /**
     * 총 주문 금액 계산
     */
    public int getTotalAmount() {
        return orderItems != null
                ? orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum()
                : 0;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public void applyDelivery(Delivery delivery) {
        this.delivery = delivery; // 단방향이면 이 한 줄만 필요
    }


}
