package com.motionmate.domain.goods;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;


@Entity // JPA가 관리하는 테이블로 매핑
@Getter // 모든 필드에 대한 getter 자동 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자 (외부에서 new 금지)
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동 증가
    private Long id; // 상품 고유 ID

    private String name;        // 상품 이름
    private String description; // 상품 설명
    private String imageUrl;    // 상품 이미지 URL
    private Integer stock;      // 상품 재고 수량
    private Integer price;      // 상품 가격

    // 필수값을 받는 생성자 (서비스 계층에서 사용)
    public Goods(String name, String description, String imageUrl, Integer stock, Integer price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.price = price;
    }

    // 주문 등으로 재고 차감 시 사용하는 비즈니스 메서드
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stock -= quantity;
    }
}

