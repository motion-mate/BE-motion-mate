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

    //S3 임시 경로에 파일을 업로드(미리보기용)
    public ResponseEntity<?> uploadTempFile(MultipartFile multipartFile){
        int userPk = 102;
        S3FileResponse result = s3Utils.uploadToTemp(multipartFile, userPk);
        return ResponseEntity.ok(result);
    }
}
