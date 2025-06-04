package com.motionmate.service.goods;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class BannerS3Service {

    private final S3ServiceUtils s3Utils;

    public ResponseEntity<?> uploadTempFile(MultipartFile multipartFile) {
        int userPk = 103; // 임의 값 (로그인 유저 기반이면 변경 가능)
        S3FileResponse result = s3Utils.uploadToTemp(multipartFile, userPk);
        return ResponseEntity.ok(result);
    }
}
