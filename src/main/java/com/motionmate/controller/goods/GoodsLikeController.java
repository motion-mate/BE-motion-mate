package com.motionmate.controller.goods;

import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.product.GoodsResponseDto;
import com.motionmate.service.goods.GoodsLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods/like")
@RequiredArgsConstructor
public class GoodsLikeController {

    private final GoodsLikeService goodsLikeService;

    // ✅ 상품 찜 추가
    @PostMapping("/{goodsId}")
    public void likeGoods(@AuthenticationPrincipal User user,
                          @PathVariable Long goodsId) {
        goodsLikeService.likeGoods(user, goodsId);
    }

    // ✅ 찜 취소
    @DeleteMapping("/{goodsId}")
    public void unlikeGoods(@AuthenticationPrincipal User user,
                            @PathVariable Long goodsId) {
        goodsLikeService.unlikeGoods(user, goodsId);
    }

    // ✅ 내가 찜한 목록 조회
    @GetMapping("/me")
    public List<GoodsResponseDto> getMyLikes(@AuthenticationPrincipal User user) {
        return goodsLikeService.getMyLikedGoods(user);
    }
}
