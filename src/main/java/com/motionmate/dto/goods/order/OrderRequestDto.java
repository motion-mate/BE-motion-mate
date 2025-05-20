package com.motionmate.dto.goods.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "주문 상품 목록은 필수입니다.")
    private List<@Valid OrderItemRequestDto> items;
}
