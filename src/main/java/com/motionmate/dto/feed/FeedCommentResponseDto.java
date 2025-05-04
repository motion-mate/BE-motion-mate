package com.motionmate.dto.feed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.motionmate.domain.feed.FeedComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FeedCommentResponseDto {

    private final Long id;

    private final String content;

    private final String writer;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

}
