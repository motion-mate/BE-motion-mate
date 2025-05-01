package com.motionmate.dto.goods.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter // 모든 필드의 getter 자동 생성
@AllArgsConstructor // 모든 필드를 받는 생성자 생성
@Builder // 빌더 패턴으로 객체 생성 가능
public class GoodsResponseDto {

    private Long id;          // 상품 ID
    private String name;      // 상품 이름
    private String description; // 상품 설명
    private String imageUrl;  // 상품 이미지 URL
    private int price;        // 상품 가격
    private int stock;        // 상품 재고
    private boolean liked; // ✅ 찜 여부 필드 추가
}
