package com.motionmate.dto.feed;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedCommentUpdateDto {
    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isAuthor;
}
