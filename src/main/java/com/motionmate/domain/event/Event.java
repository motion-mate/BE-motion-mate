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
    private String orgName;
    private String bucketKey;
    @Enumerated(EnumType.STRING)
    private EventType type;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean hidden;

    private boolean active;
    private boolean manualDeactivated;

    private Integer stock;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Event(String title, String description, String imageUrl, String orgName, String bucketKey,
                 EventType type, LocalDate startDate, LocalDate endDate,
                 boolean hidden, boolean active, boolean manualDeactivated, Integer stock) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.orgName = orgName;
        this.bucketKey = bucketKey;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hidden = hidden;
        this.active = active;
        this.manualDeactivated = manualDeactivated;
        this.stock = stock;
    }

    public void decreaseStock() {
        if (this.stock == null || this.stock <= 0) {
            throw new IllegalStateException("잔여 수량이 없습니다.");
        }
        this.stock--;
        updateActiveStatus(); // ✅ 여기서도 상태 자동 갱신
    }

    public void updateActiveStatus() {
        LocalDate today = LocalDate.now();
        this.active =
                !this.hidden &&
                        !this.manualDeactivated &&
                        (today.isEqual(startDate) || today.isAfter(startDate)) &&
                        (today.isBefore(endDate) || today.isEqual(endDate)) &&
                        (this.stock == null || this.stock > 0);
    }

    public void deactivate() {
        this.active = false;
    }

    public void hide() {
        this.hidden = true;
    }
    public void unhide() {
        this.hidden = false;
    }

    public void update(String title, String description, String imageUrl, String orgName, String bucketKey,
                       EventType type, LocalDate startDate, LocalDate endDate, Integer stock) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.orgName = orgName;
        this.bucketKey = bucketKey;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.stock = stock;
    }
}