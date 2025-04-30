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
public class FeedComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Feed feed;

    private String content;


    private LocalDateTime createdAt;

    public FeedComment(User user, Feed feed, String content) {
        this.user = user;
        this.feed = feed;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
