package com.motionmate.domain.feed;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String imageUrl;
    private String description;
    private LocalDateTime createdAt;

    public Feed(User user, String imageUrl, String description) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}