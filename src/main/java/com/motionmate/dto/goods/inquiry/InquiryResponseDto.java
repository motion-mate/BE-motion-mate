package com.motionmate.dto.goods.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InquiryResponseDto {

    private Long id;
    private String title;
    private String category;
    private String date;
    private String status;
    private String content; // 목록에서는 null 가능
}
