package com.motionmate.dto.feed;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedAccessType;
import com.motionmate.domain.user.User;
import lombok.Getter;

@Getter
public class FeedRequestDto {

    private String imageUrl;

    private String description;

    private FeedAccessType feedAccessType;

    public Feed toEntity(User user){
        return  Feed.builder()
                .user(user)
                .imageUrl(imageUrl)
                .description(description)
                .feedAccessType(feedAccessType)
                .build();

    }
}
