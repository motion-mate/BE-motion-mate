package com.motionmate.dto.goods.event;

import com.motionmate.domain.goods.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GoodsEventRequestDto {

    @NotBlank
    private String title;

    private String description;

    private String imageUrl;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private EventStatus status;

    @NotNull
    private Integer stock;
}
