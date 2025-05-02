// GoodsLikeService.java
package com.motionmate.service.goods;

import com.motionmate.domain.goods.*;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.mapper.goods.GoodsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsLikeService {

    private final GoodsLikeRepository goodsLikeRepository;
    private final GoodsRepository goodsRepository;

    public void likeGoods(User user, Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        boolean alreadyLiked = goodsLikeRepository.existsByUserAndGoods(user, goods);
        if (alreadyLiked) {
            throw new IllegalStateException("이미 찜한 상품입니다.");
        }

        goodsLikeRepository.save(new GoodsLike(user, goods));
    }

    public void unlikeGoods(User user, Long goodsId) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        GoodsLike like = goodsLikeRepository.findByUserAndGoods(user, goods)
                .orElseThrow(() -> new IllegalArgumentException("찜한 기록이 없습니다."));

        goodsLikeRepository.delete(like);
    }

    @Transactional(readOnly = true)
    public List<GoodsResponseDto> getMyLikedGoods(User user) {
        return goodsLikeRepository.findAllByUser(user).stream()
                .map(like -> GoodsMapper.toDto(like.getGoods(), true))
                .toList();
    }
}
