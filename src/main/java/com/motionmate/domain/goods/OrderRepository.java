package com.motionmate.domain.goods;

import com.motionmate.domain.goods.Order;
import com.motionmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);  // ✅ 사용자 주문 목록 조회
}
