package com.motionmate.service.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.GoodsLikeRepository;
import com.motionmate.domain.goods.GoodsRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.mapper.goods.GoodsMapper;
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

    public Long registerGoods(GoodsRequestDto dto) {
        Goods goods = GoodsMapper.toEntity(dto);
        return goodsRepository.save(goods).getId();
    }

    @Transactional(readOnly = true)
    public List<GoodsResponseDto> getGoodsList(User user) {
        return goodsRepository.findAll().stream()
                .map(goods -> GoodsMapper.toDto(goods, goodsLikeRepository.existsByUserAndGoods(user, goods)))
                .toList();
    }

    @Transactional(readOnly = true)
    public GoodsResponseDto getGoodsDetail(Long goodsId, User user) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        boolean liked = goodsLikeRepository.existsByUserAndGoods(user, goods);
        return GoodsMapper.toDto(goods, liked);
    }


    @Transactional
    public void updateGoods(Long id, GoodsRequestDto dto) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        goods.update(dto.getName(), dto.getDescription(), dto.getPrice(), dto.getStock());
    }

    @Transactional
    public void deleteGoods(Long id) {
        goodsRepository.deleteById(id);
    }
}
