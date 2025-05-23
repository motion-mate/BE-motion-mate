package com.motionmate.domain.event;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

     public enum EventType {
        CONTEST, GIVEAWAY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean active;

    // 선착순 재고: GIVEAWAY일 때만 사용
    private Integer stock;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Event(String title, String description, String imageUrl, EventType type,
                 LocalDate startDate, LocalDate endDate, Integer stock) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = true;
        this.stock = stock;
    }

    public void decreaseStock() {
        if (this.stock == null || this.stock <= 0) {
            throw new IllegalStateException("잔여 수량이 없습니다.");
        }
        this.stock--;
    }

    public void deactivate() {
        this.active = false;
    }

    public void update(String title, String description, String imageUrl,
                       EventType type, LocalDate startDate, LocalDate endDate, Integer stock) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.stock = stock;
    }
}