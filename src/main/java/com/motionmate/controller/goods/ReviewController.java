package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.review.ReviewRequestDto;
import com.motionmate.dto.goods.review.ReviewResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.goods.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성 (구매자만 가능)
    @PostMapping
    public ResponseEntity<Void> createReview(
            @RequestBody ReviewRequestDto dto,
            @AuthenticationPrincipal CustomOAuth2User principalUser
    ) {
        User user = principalUser.getUser();
        reviewService.createReview(dto, user);
        return ResponseEntity.ok().build();
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto dto,
            @AuthenticationPrincipal CustomOAuth2User principalUser
    ) {
        User user = principalUser.getUser();
        reviewService.updateReview(reviewId, dto, user);
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User principalUser
    ) {
        User user = principalUser.getUser();
        reviewService.deleteReview(reviewId, user);
        return ResponseEntity.ok().build();
    }

    // 특정 상품에 대한 리뷰 목록 조회
    @GetMapping("/{goodsId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(
            @PathVariable Long goodsId
    ) {
        return ResponseEntity.ok(reviewService.getReviewsForGoods(goodsId));
    }

    // 평균 평점 조회
    @GetMapping("/{goodsId}/average-rating")
    public ResponseEntity<Double> getAverageRating(
            @PathVariable Long goodsId
    ) {
        double avg = reviewService.getAverageRatingForGoods(goodsId);
        return ResponseEntity.ok(avg);
    }

    // 내가 쓴 리뷰 조회
    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(
            @AuthenticationPrincipal CustomOAuth2User principalUser
    ) {
        User user = principalUser.getUser();
        return ResponseEntity.ok(reviewService.getReviewsByUser(user));
    }
}
