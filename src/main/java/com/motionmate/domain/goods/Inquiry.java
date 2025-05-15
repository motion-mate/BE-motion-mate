package com.motionmate.domain.goods;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status;

    private LocalDateTime createdAt;
}
