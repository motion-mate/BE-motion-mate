package com.motionmate.domain.goods.event;

import com.motionmate.domain.goods.Goods;
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

    private String title;
    private String description;
    private String imageUrl;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @ManyToOne
    private Goods goods;

    private int stock; // 선착순 수량


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public GoodsEvent(String title, String description, String imageUrl,
                      LocalDate startDate, LocalDate endDate, EventStatus status, int stock) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.stock = stock;
    }

    public void updateStatus(EventStatus status) {
        this.status = status;
    }
}



