package com.motionmate.mapper.s3;

import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.exercise.S3FileResponse;

public class S3FileMapper {

    public static S3FileRequest toS3FileRequest(S3FileResponse response) {
        return new S3FileRequest(
                response.url(),
                response.bucketKey(),
                response.orgName()
        );
    }

    public static S3FileResponse toS3FileResponse(S3FileRequest request) {
        return new S3FileResponse(
                request.url(),
                request.bucketKey(),
                request.orgName()
        );
    }
}
