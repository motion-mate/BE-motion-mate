package com.motionmate.controller.admin;

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
@RequiredArgsConstructor
@RequestMapping("/api/admin/goods")
public class AdminGoodsController {

    private final GoodsService goodsService;

    @PostMapping
    public ResponseEntity<GoodsResponseDto> registerGoods(
            @RequestBody @Valid GoodsRequestDto request,
            @AuthenticationPrincipal CustomOAuth2User user
    ) {
        GoodsResponseDto response = goodsService.registerGoods(request, user.getUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<GoodsResponseDto> getAllGoodsForAdmin() {
        return goodsService.getGoodsListForAdmin();
    }

    @GetMapping("/{id}")
    public GoodsResponseDto getGoodsDetail(@PathVariable Long id) {
        return goodsService.getGoodsById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGoods(@PathVariable Long id, @RequestBody @Valid GoodsRequestDto dto) {
        goodsService.updateGoods(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> hideGoods(@PathVariable Long id) {
        goodsService.deleteGoods(id);  // 실제 삭제 X
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/unhide")
    public ResponseEntity<Void> unhideGoods(@PathVariable Long id) {
        goodsService.unhideGoods(id);
        return ResponseEntity.ok().build();
    }

    // 필요 시 관리자 전용 목록, 삭제, 수정도 여기서 확장 가능
}
