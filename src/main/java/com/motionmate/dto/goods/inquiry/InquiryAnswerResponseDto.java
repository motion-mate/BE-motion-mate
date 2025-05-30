package com.motionmate.dto.goods.inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import com.motionmate.domain.goods.Inquiry;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class InquiryAnswerResponseDto {
    private Long id;
    private String title;
    private String category;
    private String content;
    private String answer;
    private LocalDateTime answeredAt;
}
