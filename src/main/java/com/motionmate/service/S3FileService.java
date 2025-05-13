package com.motionmate.service;

import com.motionmate.domain.exercise.ExerciseListRepository;
import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3FileService {
    private final S3ServiceUtils s3Utils;

    public ResponseEntity<?> uploadTempFile(MultipartFile multipartFile){
        S3FileResponse result = s3Utils.uploadToTemp(multipartFile, 101);
        return ResponseEntity.ok(result);
    }
}
