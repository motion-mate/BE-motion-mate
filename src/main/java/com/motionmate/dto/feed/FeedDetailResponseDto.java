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

    private boolean liked;

    private int likeCount;

    private int commentCount;


    public static FeedDetailResponseDto fromEntity(Feed entity, boolean liked,int likeCount, int commentCount){
        return FeedDetailResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getUser().getProfile().getNickname())
                .profileImageUrl(entity.getUser().getProfile().getProfileImageUrl())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .feedAccessType(entity.getFeedAccessType())
                .liked(liked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();

    }
}
