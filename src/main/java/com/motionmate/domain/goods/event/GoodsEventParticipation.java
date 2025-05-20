package com.motionmate.domain.goods.event;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsEventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private GoodsEvent goodsEvent;

    private LocalDateTime participatedAt;

    @Builder
    public GoodsEventParticipation(User user, GoodsEvent goodsEvent) {
        this.user = user;
        this.goodsEvent = goodsEvent;
        this.participatedAt = LocalDateTime.now();
    }
}
