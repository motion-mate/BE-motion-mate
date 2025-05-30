package com.motionmate.dto.feed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedAccessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FeedResponseDto {

    private long id;

    private Long userId;

    private String nickname;

    private String profileImageUrl;

    private String imageUrl;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    private FeedAccessType feedAccessType;

    //좋아요 여부
    private boolean liked;

    private int likeCount;

    private int commentCount;

    private boolean isFollowing;
}
