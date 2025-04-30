package com.motionmate.dto.feed;

import com.motionmate.domain.feed.FeedAccessType;
import lombok.Getter;

@Getter

public class FeedRequestDto {

    private String imageUrl;

    private String description;

    private FeedAccessType feedAccessType;
}
