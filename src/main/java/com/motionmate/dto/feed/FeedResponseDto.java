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
public class FeedResponseDto {

    private long id;

    private String nickname;

    private String profileImageUrl;

    private String imageUrl;

    private String description;

    private LocalDateTime createdAt;

    private FeedAccessType feedAccessType;

    //좋아요 여부
    private boolean liked;

    public static FeedResponseDto fromEntity(Feed entity, boolean liked){
        return FeedResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getUser().getProfile().getNickname())
                .profileImageUrl(entity.getUser().getProfile().getProfileImageUrl())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .feedAccessType(entity.getFeedAccessType())
                .liked(liked)
                .build();
    }

    public static FeedResponseDto fromEntity(Feed entity){
        return fromEntity(entity, false);
    }
}
