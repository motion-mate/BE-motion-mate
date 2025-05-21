package com.motionmate.domain.goods.event;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;               // 이벤트명
    private String description;        // 이벤트 설명
    private String imageUrl;           // 이벤트 배너 이미지

    private String goodsName;          // 이벤트용 굿즈 이름
    private String goodsImageUrl;      // 이벤트용 굿즈 이미지
    private int eventStock;            // 이벤트 재고

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public GoodsEvent(String title, String description, String imageUrl,
                      String goodsName, String goodsImageUrl, int eventStock,
                      LocalDate startDate, LocalDate endDate, EventStatus status) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.goodsName = goodsName;
        this.goodsImageUrl = goodsImageUrl;
        this.eventStock = eventStock;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public void updateStatus(EventStatus status) {
        this.status = status;
    }

    public void decreaseStock(int quantity) {
        if (this.eventStock < quantity) {
            throw new IllegalArgumentException("이벤트 재고 부족");
        }
        this.eventStock -= quantity;
    }
}
