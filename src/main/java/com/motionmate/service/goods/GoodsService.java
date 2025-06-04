package com.motionmate.service.goods;

import com.motionmate.domain.goods.Goods;
import com.motionmate.domain.goods.GoodsLikeRepository;
import com.motionmate.domain.goods.GoodsRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.mapper.goods.GoodsMapper;
import com.motionmate.mapper.s3.S3FileMapper;
import com.motionmate.service.redis.LimitedGoodsRedisService;
import com.motionmate.service.s3.S3FileService;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsLikeRepository goodsLikeRepository;
    private final LimitedGoodsRedisService limitedGoodsRedisService;
    private final S3ServiceUtils s3ServiceUtils;

    int userPk = 103;

    public GoodsResponseDto registerGoods(GoodsRequestDto dto, User user) {
        S3FileRequest imageInfo = dto.getImage();

        if (imageInfo != null) {
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(imageInfo, userPk);
            imageInfo = S3FileMapper.toS3FileRequest(moved);
            s3ServiceUtils.deleteUserTempFiles(userPk);
        }

        Goods goods = GoodsMapper.toEntity(dto, imageInfo);
        Goods saved = goodsRepository.save(goods);

        if (saved.isLimited()) {
            limitedGoodsRedisService.setInitialStock(saved.getId(), saved.getStock());
        }

        return GoodsMapper.toDto(saved, false, limitedGoodsRedisService);
    }




    @Transactional(readOnly = true)
    public List<GoodsResponseDto> getGoodsList(User user) {
        return goodsRepository.findAllByHiddenFalse().stream()
                .map(goods -> GoodsMapper.toDto(goods, goodsLikeRepository.existsByUserAndGoods(user, goods), limitedGoodsRedisService))
                .toList();
    }

    @Transactional(readOnly = true)
    public GoodsResponseDto getGoodsDetail(Long goodsId, User user) {
        Goods goods = goodsRepository.findById(goodsId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        boolean liked = goodsLikeRepository.existsByUserAndGoods(user, goods);
        return GoodsMapper.toDto(goods, liked, limitedGoodsRedisService);
    }

    @Transactional
    public void updateGoods(Long id, GoodsRequestDto dto) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        goods.update(dto.getName(), dto.getDescription(), dto.getPrice(), dto.getStock());
    }

    @Transactional
    public void deleteGoods(Long id) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // ✅ 실제 삭제하지 않고 숨김 처리
        goods.hide();

        // ✅ S3 파일도 삭제
//        if (goods.getBucketKey() != null) {
//            s3FileService.deleteFile(goods.getBucketKey());
//        }
    }


    @Transactional
    public void unhideGoods(Long id) {
        Goods goods = goodsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        goods.unhide();
    }

    public List<GoodsResponseDto> getRecommendedGoods(){
        List<Goods> goodsList = goodsRepository.findTop3ByOrderByCreatedAtDesc();
        return GoodsMapper.toDtoList(goodsList, limitedGoodsRedisService);
    }
}
