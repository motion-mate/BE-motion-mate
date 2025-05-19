package com.motionmate.dto.goods.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "상품 목록은 필수입니다.")
    private List<OrderItemRequest> items;

    @Getter
    @NoArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long goodsId;

        @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
        private int quantity;
    }
}
