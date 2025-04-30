package com.motionmate.service.goods;

import com.motionmate.domain.goods.*;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsLikeRepository goodsLikeRepository;

    // ✅ 상품 등록
    public Long registerGoods(GoodsRequestDto dto) {
        Goods goods = new Goods(
                dto.getName(),
                dto.getDescription(),
                dto.getImageUrl(),
                dto.getStock(),
                dto.getPrice()
        );
        return goodsRepository.save(goods).getId();
    }

    // ✅ 전체 상품 조회 (찜 여부 포함)
    @Transactional(readOnly = true)
    public List<GoodsResponseDto> getGoodsList(User user) {
        return goodsRepository.findAll().stream()
                .map(goods -> GoodsResponseDto.builder()
                        .id(goods.getId())
                        .name(goods.getName())
                        .description(goods.getDescription())
                        .imageUrl(goods.getImageUrl())
                        .price(goods.getPrice())
                        .stock(goods.getStock())
                        .liked(goodsLikeRepository.existsByUserAndGoods(user, goods))
                        .build())
                .toList();
    }

    // ✅ 단일 상품 상세 조회 (찜 여부 포함)
    @Transactional(readOnly = true)
    public GoodsResponseDto getGoodsDetail(Long goodsId, User user) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        boolean liked = goodsLikeRepository.existsByUserAndGoods(user, goods);

        return GoodsResponseDto.builder()
                .id(goods.getId())
                .name(goods.getName())
                .description(goods.getDescription())
                .imageUrl(goods.getImageUrl())
                .price(goods.getPrice())
                .stock(goods.getStock())
                .liked(liked)
                .build();
    }
}
