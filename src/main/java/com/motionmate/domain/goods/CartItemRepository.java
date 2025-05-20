package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface
CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);
    // 특정 유저의 장바구니 목록
    List<CartItem> findAllByUser(User user);

    // 유저 + 상품 기준으로 기존 장바구니 항목 존재 여부 확인
    Optional<CartItem> findByUserAndGoods(User user, Goods goods);

    // 장바구니 항목 삭제 (유저 검증 포함용)
    void deleteByUserAndId(User user, Long id);
}
