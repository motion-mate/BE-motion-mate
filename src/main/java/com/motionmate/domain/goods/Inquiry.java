package com.motionmate.domain.goods;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inquiry {

    public enum InquiryStatus {
        BEFORE("미답변"),
        COMPLETE("답변 완료");

        private final String label;

        InquiryStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public void registerAnswer(String answer){
        this.answer = answer;
        this.answeredAt = LocalDateTime.now();
        this.status = InquiryStatus.COMPLETE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 문의 답변용 필드
    private String answer;
    private LocalDateTime answeredAt;
}

