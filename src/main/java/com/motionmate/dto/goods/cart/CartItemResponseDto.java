package com.motionmate.dto.goods.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter // 모든 필드의 getter 자동 생성
@AllArgsConstructor // 전체 필드를 매핑하는 생성자 자동 생성
@Builder // builder 패턴으로 응답 객체 유연하게 생성 가능
public class CartItemResponseDto {

    private Long cartItemId; // 장바구니 항목 고유 ID
    private Long goodsId;    // 담긴 상품 ID
    private String name;     // 상품 이름
    private String imageUrl; // 상품 이미지
    private int price;       // 상품 가격
    private int quantity;    // 담긴 수량
    private String size;
    private String color;
    private boolean soldOut;
}
