package com.motionmate.mapper;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedImage;
import com.motionmate.domain.user.User;
import com.motionmate.dto.feed.FeedDetailResponseDto;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;

public class FeedMapper {

    //FeedRequestDto -> entity
    public static Feed toEntity(FeedRequestDto dto, User user){
        return  Feed.builder()
                .user(user)
                .description(dto.getDescription())
                .feedAccessType(dto.getFeedAccessType())
                .build();
    }

    //entity -> FeedResponseDto
    public static FeedResponseDto fromEntity(Feed entity, boolean liked, int likeCount, int commentCount, boolean isFollowing){
        String imageUrl = entity.getImages().stream().findFirst().map(FeedImage::getUrl).orElse(null);

        return FeedResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .nickname(entity.getUser().getProfile().getNickname())
                .profileImageUrl(entity.getUser().getProfile().getProfileImageUrl())
                .imageUrl(imageUrl)
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .feedAccessType(entity.getFeedAccessType())
                .liked(liked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .isFollowing(isFollowing)
                .build();
    }

    //entity -> FeedResponseDto
    public static FeedResponseDto fromEntity(Feed entity){
        return fromEntity(entity, false, 0, 0, false);
    }

    //entity -> FeedDetailResponseDto
    public static FeedDetailResponseDto fromEntityDetail(Feed entity, boolean liked, int likeCount, int commentCount, boolean isFollowing, Long loginUserId){
        String imageUrl = entity.getImages().stream().findFirst().map(FeedImage::getUrl).orElse(null);
        boolean isAuthor = entity.getUser().getId().equals(loginUserId);

        return FeedDetailResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .nickname(entity.getUser().getProfile().getNickname())
                .profileImageUrl(entity.getUser().getProfile().getProfileImageUrl())
                .imageUrl(imageUrl)
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .feedAccessType(entity.getFeedAccessType())
                .liked(liked)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .isAuthor(isAuthor)
                .isFollowing(isFollowing)
                .build();
    }

    // 오버로딩: isFollowing 기본 false
    public static FeedResponseDto fromEntityLikeMyFeed(Feed feed, boolean liked, int likeCount, int commentCount) {
        return fromEntity(feed, liked, likeCount, commentCount, false);
    }
}
