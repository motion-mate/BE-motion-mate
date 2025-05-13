package com.motionmate.dto.exercise;

public record SaveImageResponse(
        String url,
        String bucketKey,
        String orgName
) {

}
