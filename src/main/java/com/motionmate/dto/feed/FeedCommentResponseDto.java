package com.motionmate.dto.feed;

import com.motionmate.domain.feed.FeedComment;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
public class FeedCommentResponseDto {

    private final Long id;

    private final String content;

    private final String writer;

    private final LocalDateTime createdAt;

    public FeedCommentResponseDto(FeedComment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getUser().getProfile().getNickname();
        this.createdAt = comment.getCreatedAt();

    }

}
