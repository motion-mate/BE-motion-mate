package com.motionmate.service.goods;

import com.motionmate.domain.goods.*;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.review.ReviewRequestDto;
import com.motionmate.dto.goods.review.ReviewResponseDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.goods.ReviewMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final GoodsRepository goodsRepository;

    /**
     * 리뷰 작성 메서드
     * - 사용자가 해당 상품을 주문한 적이 있어야 작성 가능
     * - 동일 상품에 중복 리뷰 작성은 불가
     */
    public void createReview(ReviewRequestDto dto, User user) {
        // 1. 상품 존재 여부 확인
        Goods goods = goodsRepository.findById(dto.getGoodsId())
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        // 2. 사용자 주문 내역 확인 (구매자만 작성 가능)
        boolean hasOrdered = orderRepository.existsByUserAndGoods(user, goods);
        if (!hasOrdered) {
            throw new RuntimeException("해당 상품을 주문한 사용자만 리뷰를 작성할 수 있습니다.");
        }

        // 3. 중복 리뷰 방지
        if (reviewRepository.existsByUserAndGoods(user, goods)) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 이 상품에 리뷰를 작성했습니다.");
        }

        // 4. 리뷰 저장
        Review review = ReviewMapper.toEntity(dto, user, goods);
        reviewRepository.save(review);
    }

    /**
     * 리뷰 수정 메서드
     */
    @Transactional
    public void updateReview(Long reviewId, ReviewRequestDto dto, User user) {
        // 1. 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

        // 2. 작성자 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("리뷰 작성자만 수정할 수 있습니다.");
        }

        // 3. 내용 및 평점 수정
        review.update(dto.getContent(), dto.getRating());
    }

    /**
     * 리뷰 삭제 메서드
     */
    @Transactional
    public void deleteReview(Long reviewId, User user){
        Review review = reviewRepository.findById((reviewId))
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));

        if (!review.getUser().getId().equals(user.getId())){
            throw new RuntimeException("리뷰 작성자만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }

    /**
     * 특정 상품에 대한 리뷰 목록 조회
     */
    public List<ReviewResponseDto> getReviewsForGoods(Long goodsId) {
        // 1. 상품 존재 여부 확인
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        // 2. 해당 상품에 대한 모든 리뷰 조회 및 DTO 변환
        return reviewRepository.findAllByGoods(goods).stream()
                .map(ReviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 평균 평점 계산
     */
    public double getAverageRatingForGoods(Long goodsId){
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(()-> new RuntimeException("상품이 존해하지 않습니다."));

        return reviewRepository.calculateAverageRating(goods);
    }

}
