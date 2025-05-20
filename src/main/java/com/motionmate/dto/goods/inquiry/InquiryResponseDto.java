package com.motionmate.dto.goods.inquiry;

import com.motionmate.domain.goods.Inquiry;
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
    private Inquiry.InquiryStatus status;
    private String statusLabel; // ✅ 한글 상태 설명
    private String content;     // 목록에서는 null 가능
}
