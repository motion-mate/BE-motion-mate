package com.motionmate.mapper;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.user.User;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;

public class FeedMapper {

    //FeedRequestDto -> entity
    public static Feed toEntity(FeedRequestDto dto, User user){
        return  Feed.builder()
                .user(user)
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .feedAccessType(dto.getFeedAccessType())
                .build();
    }

    //entity -> FeedResponseDto
    public static FeedResponseDto fromEntity(Feed entity, boolean liked){
        return FeedResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getUser().getProfile().getNickname())
                .profileImageUrl(entity.getUser().getProfile().getProfileImageUrl())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .feedAccessType(entity.getFeedAccessType())
                .liked(liked)
                .build();
    }

    //entity -> FeedResponseDto
    public static FeedResponseDto fromEntity(Feed entity){
        return fromEntity(entity, false);
    }

    //entity -> FeedDetailResponseDto
    public static FeedDetailResponseDto fromEntity(Feed entity, boolean liked, int likeCount, int commentCount){
        return FeedDetailResponseDto.builder()
                .id(entity.getId())
                .nickname(entity.getUser().getProfile().getNickname())
                .profileImageUrl(entity.getUser().getProfile().getProfileImageUrl())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .feedAccessType(entity.getFeedAccessType())
                .liked(liked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();

    }
}
