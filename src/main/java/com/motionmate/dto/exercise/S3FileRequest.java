package com.motionmate.dto.exercise;

public record S3FileRequest (
    String url,
    String bucketKey,
    String orgName
){}
