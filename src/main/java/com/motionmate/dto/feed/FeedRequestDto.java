package com.motionmate.dto.feed;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedAccessType;
import com.motionmate.domain.user.User;
import com.motionmate.dto.exercise.S3FileRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedRequestDto {

    private S3FileRequest imageUrl;

    private String description;

    private FeedAccessType feedAccessType;

}
