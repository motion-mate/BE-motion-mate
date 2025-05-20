package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    private LocalDateTime orderedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String trackingNumber;
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
        return orderItems != null
                ? orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum()
                : 0;
    }
}
