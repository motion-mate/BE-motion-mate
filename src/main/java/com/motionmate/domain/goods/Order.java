package com.motionmate.domain.goods;

import com.motionmate.domain.user.User; // ✅ User 클래스 import
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity // JPA 엔티티 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 (외부 new 방지)
@Table(name = "orders") // 테이블명 명시 (예약어 order 피하기 위함)
public class Order {

    public enum OrderStatus {
        READY, GOING, FINISH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 PK
    private Long id; // 주문 ID

    @ManyToOne // 주문한 유저와 다대일 관계
    private User user;

    @ManyToOne // 주문한 상품과 다대일 관계
    private Goods goods;

//    private Integer price;
    private Integer quantity;           // 주문 수량
    private LocalDateTime orderedAt;    // 주문 시각

    // 배송 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 송장번호
    private String trackingNumber;

    // 택배사 이름 or 코드
    private String courier;

    // 주문 생성자 (user, goods, quantity 필수)
    @Builder
    public Order(User user, Goods goods, Integer quantity) {
        this.user = user;
        this.goods = goods;
        this.quantity = quantity;
        this.orderedAt = LocalDateTime.now(); // 주문 생성 시 자동 기록
//        this.price = goods.getPrice()*quantity;
    }
}
