package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.goods.GoodsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    // ✅ 상품 등록
    @PostMapping
    public Long registerGoods(@RequestBody @Valid GoodsRequestDto dto) {
        return goodsService.registerGoods(dto);
    }

    // ✅ 전체 상품 목록 조회
    @GetMapping
    public List<GoodsResponseDto> getAllGoods(@AuthenticationPrincipal CustomOAuth2User user) {
        return goodsService.getGoodsList(user.getUser());
    }

    // ✅ 특정 상품 상세 조회
    @GetMapping("/{id}")
    public GoodsResponseDto getGoodsDetail(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomOAuth2User user) {
        return goodsService.getGoodsDetail(id, user.getUser());
    }
}
