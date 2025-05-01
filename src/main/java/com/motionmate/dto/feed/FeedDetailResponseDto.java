package com.motionmate.dto.feed;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedAccessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FeedDetailResponseDto {
    private long id;

    private String nickname;

    private String profileImageUrl;

    private String imageUrl;

    private String description;

    private LocalDateTime createdAt;

    private FeedAccessType feedAccessType;

    private int likeCount;

    private int commentCount;

    public static FeedDetailResponseDto fromEntity(Feed entity){
        return FeedDetailResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getUser().getNickname())
                .profileImageUrl(entity.getUser().getProfileImageUrl())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .feedAccessType(entity.getFeedAccessType())
                .build();

    }
}
