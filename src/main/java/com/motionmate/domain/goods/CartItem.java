package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 new 막고 JPA용 기본 생성자 허용
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 장바구니 주인 (유저)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 장바구니에 담긴 상품
    @ManyToOne(fetch = FetchType.LAZY)
    private Goods goods;

    // 수량
    private int quantity;

    // 사이즈
    private String size;

    // 컬러
    private String color;

    public CartItem(User user, Goods goods, int quantity, String size, String color) {
        this.user = user;
        this.goods = goods;
        this.quantity = quantity;
        this.size = size;
        this.color = color;
    }

    // 수량 업데이트 (예: 기존 항목 수량 변경 시)
    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
