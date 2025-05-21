package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserAndGoods(User user, Goods goods);
    List<Review> findAllByGoods(Goods goods);

    // 평균 평점 계산 쿼리
    @Query("SELECT COALESCE(AVG(r.rating),0) FROM Review r WHERE r.goods = :goods")
    double calculateAverageRating(@Param("goods") Goods goods);
}
