package com.motionmate.dto.goods.review;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponseDto {
 private Long reviewId;
 private String content;
 private int rating;
 private String username;
 private LocalDateTime createdAt;
}
