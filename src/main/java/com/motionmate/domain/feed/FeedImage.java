package com.motionmate.domain.feed;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feedImage")
public class FeedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String bucketKey;
    @Column(nullable = false)
    private String orgName;

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @Builder
    public FeedImage(String url, String bucketKey, String orgName, Feed feed) {
        this.url = url;
        this.bucketKey = bucketKey;
        this.orgName = orgName;
        this.feed = feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }





}
