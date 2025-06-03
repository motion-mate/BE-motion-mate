package com.motionmate.service.user;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileImageUploadService {

    private final S3ServiceUtils s3ServiceUtils;

    public S3FileResponse uploadToTemp(MultipartFile file, int userPk) {
        return s3ServiceUtils.uploadToTemp(file, userPk);
    }
}
