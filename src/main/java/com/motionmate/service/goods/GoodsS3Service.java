package com.motionmate.service.goods;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class GoodsS3Service {

    private final S3ServiceUtils s3Utils;

    //S3 임시 경로(temp)에 이미지를 업로드(미리보기용)
    public ResponseEntity<?> uploadTempFile(MultipartFile multipartFile){
        int userPk = 103;
        S3FileResponse result = s3Utils.uploadToTemp(multipartFile, userPk);
        return ResponseEntity.ok(result);
    }
}
