package com.motionmate.dto.exercise;


import lombok.Builder;
import lombok.Getter;


@Builder
public record S3FileResponse (
    String url,
    String bucketKey,
    String orgName
){}
