package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.Review;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.review.ReviewRequestDto;
import com.motionmate.dto.goods.review.ReviewResponseDto;

import java.time.LocalDateTime;

public class ReviewMapper {
    // 리뷰 생성(리뷰 작성시)
    public static Review toEntity(ReviewRequestDto dto, User user, Goods goods){
        return Review.builder()
                .user(user)
                .goods(goods)
                .rating(dto.getRating())
                .content(dto.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 리뷰 -> 리뷰 DTO 변환
    public static ReviewResponseDto toResponse(Review review){
        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .username(review.getUser().getOauthNickname())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
