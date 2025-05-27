package com.motionmate.controller.admin;

import com.motionmate.service.goods.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final OrderService orderService;

    // ✅ 한정 상품 Redis 재고 조회
    @GetMapping("/limited-stock/{goodsId}")
    public ResponseEntity<Integer> getLimitedStock(@PathVariable Long goodsId) {
        int stock = orderService.getLimitedGoodsStock(goodsId);
        return ResponseEntity.ok(stock);
    }

    // ✅ 한정 상품 Redis 재고 수동 수정
    @PatchMapping("/limited-stock/{goodsId}")
    public ResponseEntity<Void> updateLimitedStock(@PathVariable Long goodsId,
                                                   @RequestParam int quantity) {
        orderService.updateLimitedGoodsStock(goodsId, quantity);
        return ResponseEntity.ok().build();
    }
}
