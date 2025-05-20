package com.motionmate.domain.goods;

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

<<<<<<< HEAD
    @Enumerated(EnumType.STRING) // ✅ Enum은 문자열로 저장
    private OrderStatus status;

    private LocalDateTime orderedAt;

    private String trackingNumber;

=======
    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String trackingNumber;
>>>>>>> 37bf0df6c36ca39daeaa6c15258b39fec675af8e
    private String courier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
<<<<<<< HEAD
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
=======
        return orderItems != null
                ? orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum()
                : 0;
>>>>>>> 37bf0df6c36ca39daeaa6c15258b39fec675af8e
    }
}
