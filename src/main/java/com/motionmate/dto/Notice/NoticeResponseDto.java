package com.motionmate.dto.Notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String  content;
    private String createdAt;
}
