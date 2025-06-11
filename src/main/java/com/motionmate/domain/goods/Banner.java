package com.motionmate.domain.goods;

import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.goods.banner.BannerRequestDto;
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
    private String orgName;

    private Integer orderIndex;
    private boolean visible;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        // DB에 엔티티가 처음 저장되기 직전(createdAt이 null일 경우) 현재 시간을 createdAt에 할당
        this.createdAt = LocalDateTime.now();

        // visible이 true이고, startDate가 지정되지 않았을 경우 시작일을 현재 시간으로 자동 지정
        if (visible && startDate == null) {
            this.startDate = LocalDateTime.now();
        }
    }

    public void updateInfo(BannerRequestDto dto) {
        this.title = dto.getTitle();
        this.link = dto.getLink();
        this.orderIndex = dto.getOrderIndex();
        this.visible = dto.isVisible();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
    }

    public void updateImage(S3FileRequest image) {
        this.imageUrl = image.url();
        this.bucketKey = image.bucketKey();
        this.orgName = image.orgName();
    }

}
