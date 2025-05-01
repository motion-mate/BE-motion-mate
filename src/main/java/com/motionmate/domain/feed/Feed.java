package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private FeedAccessType feedAccessType;

    public Feed(User user, String imageUrl, String description, FeedAccessType feedAccessType) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.feedAccessType = feedAccessType;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}