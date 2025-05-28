package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.product.GoodsRequestDto;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.goods.GoodsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return goodsService.getGoodsList((user != null) ? user.getUser() : null);
    }

    // ✅ 특정 상품 상세 조회
    @GetMapping("/{id}")
    public GoodsResponseDto getGoodsDetail(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomOAuth2User user) {
        return goodsService.getGoodsDetail(id, user.getUser());
    }

    // ✅ 상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGoods(@PathVariable Long id,
                                            @RequestBody @Valid GoodsRequestDto dto) {
        goodsService.updateGoods(id, dto);
        return ResponseEntity.ok().build();
    }

    // ✅ 상품 숨김 처리 (Soft Delete)
    @PatchMapping("/{id}/hide")
    public ResponseEntity<Void> hideGoods(@PathVariable Long id) {
        goodsService.deleteGoods(id); // 내부에서는 goods.hide() 호출
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unhide")
    public ResponseEntity<Void> unhideGoods(@PathVariable Long id) {
        goodsService.unhideGoods(id);
        return ResponseEntity.ok().build();
    }

    // 추천 상품
    @GetMapping("/recommend")
    public List<GoodsResponseDto> getRecommendedGoods(){
        return goodsService.getRecommendedGoods();
    }


}
