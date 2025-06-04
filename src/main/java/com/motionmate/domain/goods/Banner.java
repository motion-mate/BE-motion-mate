package com.motionmate.domain.goods;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;           // ✅ 배너 제목
    private String link;            // ✅ 클릭 시 이동할 링크

    private String imageUrl;
    private String bucketKey;

    private Integer orderIndex;
    private boolean visible;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        if (visible && startDate == null) {
            this.startDate = LocalDateTime.now();
        }
    }
}
