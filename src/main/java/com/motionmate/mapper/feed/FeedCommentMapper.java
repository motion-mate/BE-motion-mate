package com.motionmate.mapper.feed;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedComment;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfile;
import com.motionmate.dto.feed.FeedCommentRequestDto;
import com.motionmate.dto.feed.FeedCommentResponseDto;

public class FeedCommentMapper {

    //dto -> entity
    public static FeedComment toEntity(FeedCommentRequestDto dto, User user, Feed feed){
        return new FeedComment(user, feed, dto.getContent());
    }

    //entity -> FeedCommentResponseDto
    //댓글 등록/수정용
    public static FeedCommentResponseDto fromEntity(FeedComment entity, Long currentUserId){
      boolean isAuthor = entity.getUser().getId().equals(currentUserId);
      UserProfile profile = entity.getUser().getProfile();

        return new FeedCommentResponseDto(
                entity.getId(),
                entity.getContent(),
                profile != null ? profile.getNickname() : "알 수 없음",
                profile != null ? profile.getProfileImageUrl() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                isAuthor
        );
    }

    public static FeedCommentResponseDto fromEntity(FeedComment entity){

        return new FeedCommentResponseDto(
                entity.getId(),
                entity.getContent(),
                entity.getUser().getProfile().getNickname(),
                entity.getUser().getProfile().getProfileImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                true
        );
    }



}
