package com.motionmate.controller.s3;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profileimages")
public class ProfileImageUploadController {

    private final S3ServiceUtils s3ServiceUtils;

    @PostMapping("/temp")
    public ResponseEntity<S3FileResponse> uploadTempImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userPk") int userPk) {

        S3FileResponse response = s3ServiceUtils.uploadToTemp(file, userPk);
        return ResponseEntity.ok(response);
    }
}
