package com.motionmate.dto.goods.inquiry;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryAnswerRequestDto {

    @NotBlank
    private String answer;
}
