package com.motionmate.utils;


import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.dto.exercise.S3FileRequest;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Component
@RequiredArgsConstructor

public class S3ServiceUtils {
    // S3 작업을 위한 핵심 컴포넌트
    private final S3Template s3Template;
    private final S3Client s3Client;

    // application.properties에서 설정된 S3 버킷 이름
    @Value("${spring.cloud.aws.s3.bucket}")
    private String BUCKET_NAME;
    // 임시 파일 저장 경로
    @Value("${spring.cloud.aws.s3.temp}")
    private String TEMP_PATH;

    // 실제 파일 저장 경로
    @Value("${spring.cloud.aws.s3.upload}")
    private String UPLOAD_PATH;

    public S3FileResponse uploadToTemp(MultipartFile file, int userPk) {
        try {
            // 임시 파일 경로 생성 (사용자PK/파일명) // temp/101/
            //String tempKey = TEMP_PATH + userPk + "/" + createUniqueFileName(file.getOriginalFilename());
            String tempKey = TEMP_PATH + userPk + "/" + newFileNameByNanotime(file.getOriginalFilename());

            // 메타데이터 설정 (Content-Type, 퍼블릭 액세스 등)
            ObjectMetadata metadata = ObjectMetadata.builder().contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ).build();

            // S3에 파일 업로드
            S3Resource s3Resource = s3Template.upload(BUCKET_NAME, tempKey, file.getInputStream(), metadata);
            //return s3Resource.getURL().toString().substring(6);
            return S3FileResponse.builder()
                    .url(s3Resource.getURL().toString().substring(6))
                    .orgName(file.getOriginalFilename())
                    .bucketKey(tempKey)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("임시 파일 업로드 실패", e);
        }
    }
    public S3FileResponse moveFromTempToUpload(S3FileRequest s3FileRequest, int userPk) {
        // tempUrl에서 키 추출
        String tempKey = s3FileRequest.url();

        // 실제 저장 경로 생성
        String uploadKey = UPLOAD_PATH + userPk + tempKey.substring(tempKey.lastIndexOf("/"));

        // 파일 이동
        return moveFile(s3FileRequest.bucketKey(), uploadKey, s3FileRequest);
    }

    public void deleteUserTempFiles(int userPk) {
        try {
            // 사용자의 임시 폴더 경로
            String userTempPath = TEMP_PATH + userPk + "/";

            s3Client.listObjectsV2Paginator(ListObjectsV2Request.builder()
                            .bucket(BUCKET_NAME).prefix(userTempPath)
                            .build()).stream()
                    .flatMap(response->response.contents().stream())
                    .forEach(s3Object->{
                        s3Client.deleteObject(builder->builder.bucket(BUCKET_NAME).key(s3Object.key()).build());
                    });
        } catch (Exception e) {
            throw new RuntimeException("임시 파일 삭제 실패", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            String bucketKey = createUniqueFileName(file.getOriginalFilename());

            ObjectMetadata metadata = ObjectMetadata.builder().contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ).build();
            S3Resource s3Resource = s3Template.upload(BUCKET_NAME, bucketKey, file.getInputStream(),
                    metadata);

            //System.out.println(s3Resource.getURL().toString().substring(6));
            return s3Resource.getURL().toString().substring(6);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
    private String createUniqueFileName(String originalFileName) {
        return UUID.randomUUID().toString() + getFileExtension(originalFileName);
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new RuntimeException("잘못된 형식의 파일입니다.");
        }
    }
    //파일이름 nanoTime()을 이용하여 변경
    private static String newFileNameByNanotime(String orgName) {
        int idx=orgName.lastIndexOf(".");
        return orgName.substring(0, idx)+"-"+(System.nanoTime()/1000000)
                + orgName.substring(idx); //.확장자 :  .jpg
    }


    public S3FileResponse moveFile(String sourceKey, String destinationKey, S3FileRequest s3FileRequest) {
        try {
            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .sourceBucket(BUCKET_NAME)
                    .sourceKey(sourceKey)
                    .destinationBucket(BUCKET_NAME)
                    .destinationKey(destinationKey)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            CopyObjectResponse result = s3Client.copyObject(copyObjectRequest);

            if (result != null) {
                s3Template.deleteObject(BUCKET_NAME, sourceKey);

                return S3FileResponse.builder()
                        .url(s3Client.utilities()
                                .getUrl(builder -> builder.bucket(BUCKET_NAME).key(destinationKey).build()).toString()
                                .substring(6))
                        .bucketKey(destinationKey).orgName(s3FileRequest.orgName()) // 원본 파일명 추가
                        .build();
            }
            throw new RuntimeException("파일 이동 실패");
        } catch (S3Exception e) {
            throw new RuntimeException("S3 파일 이동 실패", e);
        } catch (Exception e) {
            throw new RuntimeException("파일 이동 중 오류 발생", e);
        }
    }


}
