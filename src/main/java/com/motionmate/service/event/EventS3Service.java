package com.motionmate.service.event;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class EventS3Service {

    private final S3ServiceUtils s3ServiceUtils;

    public ResponseEntity<S3FileResponse> uploadTempFile(MultipartFile multipartFile) {
        int dummyUserPk = 0; // 이벤트는 사용자 ID 없이 고정
        S3FileResponse uploaded = s3ServiceUtils.uploadToTemp(multipartFile, dummyUserPk);
        return ResponseEntity.ok(uploaded);
    }
}
