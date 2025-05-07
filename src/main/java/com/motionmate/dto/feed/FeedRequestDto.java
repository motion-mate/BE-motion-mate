package com.motionmate.dto.feed;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedAccessType;
import com.motionmate.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedRequestDto {

    private String imageUrl;

    private String description;

    private FeedAccessType feedAccessType;

}
