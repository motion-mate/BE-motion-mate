package com.motionmate.dto.goods.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long goodsId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    private int quantity;

    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private int unitPrice;
}

