package com.motionmate.mapper;

import com.motionmate.domain.exercise.ExerciseImage;
import com.motionmate.domain.exercise.ExerciseList;
import com.motionmate.domain.exercise.ExerciseSchedule;
import com.motionmate.domain.user.User;
import com.motionmate.dto.exercise.*;

import java.util.Collections;

//DTO -> Entity
public class ExerciseListMapper {
    public static ExerciseList toExerciseList(ExerciseListRequest request) {
        ExerciseImage image = toExerciseImage(request.imageUrl());

        ExerciseList exercise = new ExerciseList(
                null,
                request.name(),
                request.description(),
                request.category(),
                Collections.singletonList(image)
        );
        image.setExercise(exercise);
        return exercise;
    }
    //DTO -> Entity (이미지)
    public static ExerciseImage toExerciseImage(S3FileRequest request) {
        ExerciseImage image = new ExerciseImage();
        image.setUrl(request.url());
        image.setBucketKey(request.bucketKey());
        image.setOrgName(request.orgName());
        return image;
    }
    //Entity -> DTO (이미지 응답용)
    public static SaveImageResponse fromExerciseImage(ExerciseImage image) {
        return new SaveImageResponse(
                image.getUrl(),
                image.getBucketKey(),
                image.getOrgName()
        );
    }
    public static S3FileRequest toS3FileRequest(S3FileResponse response){
        return new S3FileRequest(
                response.url(),
                response.bucketKey(),
                response.orgName()
        );
    }
    public static ExerciseListWithImageResponse from(ExerciseList exercise, SaveImageResponse imageDto){
        return new ExerciseListWithImageResponse(
                exercise.getExerciseId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getCategory(),
                imageDto
        );
    }
    public static ExerciseSchedule toScheduleEntity(ExerciseScheduleRequest request, User user, ExerciseList exercise) {
        return new ExerciseSchedule(
                null,
                user,
                exercise,
                request.getDate(),
                request.getSetNumber(),
                request.getKg(),
                request.getReps(),
                request.getTime()
        );
    }
    public static ExerciseScheduleResponse toScheduleResponseDto(ExerciseSchedule schedule){
        return new ExerciseScheduleResponse(
                schedule.getId(),
                schedule.getExercise().getName(),
                schedule.getExercise().getCategory().name(),
                schedule.getDate(),
                schedule.getSetNumber(),
                schedule.getKg(),
                schedule.getReps(),
                schedule.getTime()
        );
    }
}
