package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
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
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private FeedAccessType feedAccessType;

    @Builder
    public Feed(User user, String imageUrl, String description, FeedAccessType feedAccessType) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.description = description;
        this.feedAccessType = feedAccessType;
    }

    public void update(String description, String imageUrl, FeedAccessType feedAccessType) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.feedAccessType = feedAccessType;
    }

}