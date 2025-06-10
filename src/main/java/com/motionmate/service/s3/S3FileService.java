package com.motionmate.service.s3;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3FileService {

    private final S3ServiceUtils s3Utils;

    // 기존: 운동 기능 등에서 임시 업로드
    public ResponseEntity<?> uploadTempFile(MultipartFile multipartFile) {
        S3FileResponse result = s3Utils.uploadToTemp(multipartFile, 101);
        return ResponseEntity.ok(result);
    }

    // ✅ Goods 전용 업로드 메서드
    public String uploadFileToGoods(MultipartFile multipartFile) throws IOException {
        S3FileResponse result = s3Utils.uploadToTemp(multipartFile, 103); // bucketKey 또는 folder 구분
        return result.url(); // 또는 필요시 getBucketKey()
    }

    public void deleteFile(String bucketKey) {
        s3Utils.deleteFile(bucketKey);
    }
}
