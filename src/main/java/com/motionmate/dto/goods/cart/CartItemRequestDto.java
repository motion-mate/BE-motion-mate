package com.motionmate.dto.goods.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // 모든 필드에 대한 getter 자동 생성
@NoArgsConstructor // 기본 생성자 (JSON → 객체 변환용)
public class CartItemRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.") // 상품 ID는 null이면 안됨
    private Long goodsId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.") // 최소 수량 1 이상
    private int quantity;
}
