package com.motionmate.domain.goods;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    private int quantity;

    private int unitPrice; // 주문 당시 단가

    /**
     * 단건 총액 계산
     */
    public int getTotalPrice() {
        return unitPrice * quantity;
    }

    /**
     * 연관관계 세팅용 (Order 쪽에서만 호출)
     */
    public void setOrder(Order order) {
        this.order = order;
    }
}
