package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Goods goods;

    private Integer quantity;
    private LocalDateTime orderedAt;

    public Order(User user, Goods goods, Integer quantity) {
        this.user = user;
        this.goods = goods;
        this.quantity = quantity;
        this.orderedAt = LocalDateTime.now();
    }
}
