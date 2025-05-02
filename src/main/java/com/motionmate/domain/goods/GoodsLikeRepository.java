package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoodsLikeRepository extends JpaRepository<GoodsLike, Long> {

    // 특정 유저가 특정 상품을 찜했는지 여부 조회
    Optional<GoodsLike> findByUserAndGoods(User user, Goods goods);

    // 유저가 찜한 전체 상품 목록
    List<GoodsLike> findAllByUser(User user);

    // 찜 여부 boolean 단순 조회용
    boolean existsByUserAndGoods(User user, Goods goods);
}
