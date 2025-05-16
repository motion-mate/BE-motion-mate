package com.motionmate.service;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FeedS3Service {
    private final S3ServiceUtils s3Utils;

    public ResponseEntity<?> uploadTempFile(MultipartFile multipartFile){
        S3FileResponse result = s3Utils.uploadToTemp(multipartFile, 102);
        return ResponseEntity.ok(result);
    }
}
