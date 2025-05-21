package com.motionmate.dto.goods.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GoodsEventRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String goodsName;         // ✅ 이벤트용 굿즈명

    @NotBlank
    private String goodsImageUrl;     // ✅ 굿즈 이미지

    @NotNull
    private Integer eventStock;       // ✅ 이벤트 재고

    @NotNull
    private LocalDate startDate;

    @NotNull
    @Future
    private LocalDate endDate;
}
