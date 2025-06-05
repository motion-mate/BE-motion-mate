package com.motionmate.controller.event;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.service.event.EventS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventS3UploadController {

    private final EventS3Service eventS3Service;

    @PostMapping("/image/temp")
    public ResponseEntity<S3FileResponse> uploadTempImage(@RequestParam("file") MultipartFile file) {
        return eventS3Service.uploadTempFile(file);
    }
}