package com.motionmate.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LimitedGoodsRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // ✅ 1. 한정 상품 재고 감소 시도 (감소 실패 시 복구)
    public boolean tryDecreaseStock(Long goodsId, int quantity) {
        String key = "LIMITED_GOODS_STOCK:" + goodsId;
        Long stock = redisTemplate.opsForValue().increment(key, -quantity);
        if (stock == null || stock < 0) {
            redisTemplate.opsForValue().increment(key, quantity); // 복구
            return false;
        }
        return true;
    }

    // ✅ 2. 한정 상품 등록 시 초기 재고 설정
    public void setInitialStock(Long goodsId, int quantity) {
        redisTemplate.opsForValue().set("LIMITED_GOODS_STOCK:" + goodsId, String.valueOf(quantity));
    }

    // ✅ 3. 현재 남은 재고 조회
    public int getStock(Long goodsId) {
        String key = "LIMITED_GOODS_STOCK:" + goodsId;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value) : 0;
    }

    // ✅ 4. 관리자 수량 재설정 (재입고)
    public void restock(Long goodsId, int quantity) {
        redisTemplate.opsForValue().set("LIMITED_GOODS_STOCK:" + goodsId, String.valueOf(quantity));
    }
}

