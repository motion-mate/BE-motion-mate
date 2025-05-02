package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feeds")
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    private String imageUrl;
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private FeedAccessType feedAccessType;

    public Feed(User user, String imageUrl, String description, FeedAccessType feedAccessType) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.description = description;
        this.feedAccessType = feedAccessType;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}