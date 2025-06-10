package com.motionmate.domain.goods;

import com.motionmate.domain.goods.Order;
import com.motionmate.domain.user.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END
        FROM Order o
        JOIN o.orderItems oi
        WHERE o.user = :user AND oi.goods = :goods
    """)
    boolean existsByUserAndGoods(@Param("user") User user, @Param("goods") Goods goods);

    List<Order> findByUser(User user);  // ✅ 사용자 주문 목록 조회

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.delivery " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.goods " +
            "WHERE o.user = :user")
    List<Order> findAllWithAllData(@Param("user") User user);
}
