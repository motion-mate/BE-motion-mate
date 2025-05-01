package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 protected로 제한 (JPA 프록시용)
public class GoodsLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 찜한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 찜한 상품
    @ManyToOne(fetch = FetchType.LAZY)
    private Goods goods;

    // 찜한 시각
    private LocalDateTime likedAt = LocalDateTime.now();

    public GoodsLike(User user, Goods goods) {
        this.user = user;
        this.goods = goods;
    }
}
