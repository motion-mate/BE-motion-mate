package com.motionmate.service.exercise;


import com.motionmate.domain.exercise.*;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.exercise.*;
import com.motionmate.mapper.exercise.ExerciseListMapper;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ExerciseListService {

    private final ExerciseListRepository exerciseListRepository;
    private final ExerciseImageRepository exerciseImageRepository;
    private final S3ServiceUtils s3ServiceUtils;
    private final ExerciseScheduleRepository exerciseScheduleRepository;
    private final UserRepository userRepository;


    public ResponseEntity<?> createExerciseList(ExerciseListRequest request){
        int userPk = 101;

        S3FileRequest tempImage = request.imageUrl();
        S3FileResponse movedImage = s3ServiceUtils.moveFromTempToUpload(tempImage, userPk);
        S3FileRequest newRequestImage = ExerciseListMapper.toS3FileRequest(movedImage);
        ExerciseListRequest newRequest = new ExerciseListRequest(
                request.name(),
                request.description(),
                request.category(),
                newRequestImage

        );

        ExerciseList exerciseList = ExerciseListMapper.toExerciseList(newRequest);
        ExerciseList saved = exerciseListRepository.save(exerciseList);
        exerciseImageRepository.saveAll(saved.getImageUrl());

        s3ServiceUtils.deleteUserTempFiles(userPk);
        return ResponseEntity.ok("진짜 성공한거니? 운동등록완료");
    }
    public List<ExerciseListWithImageResponse> getAllExerciseList(){
        List<ExerciseList> exercise = exerciseListRepository.findAll();
        return exercise.stream().map(exercises -> {
            ExerciseImage image = exerciseImageRepository.findFirstByExercise_ExerciseId(exercises.getExerciseId())
                    .orElse(null);

            SaveImageResponse imageDto = (image != null) ? ExerciseListMapper.fromExerciseImage(image) : null;

            return ExerciseListMapper.from(exercises, imageDto);
        }).collect(Collectors.toList());
    }

    public void createExerciseSchedule(List<ExerciseScheduleRequest> requests) {
        List<ExerciseSchedule> entities = requests.stream()
                .map(request -> {
                    User user = userRepository.findById(request.getUserId())
                            .orElseThrow(() -> new RuntimeException("User not found: " + request.getUserId()));
                    ExerciseList exercise = exerciseListRepository.findById(request.getExerciseId())
                            .orElseThrow(() -> new RuntimeException("Exercise not found: " + request.getExerciseId()));
                    return ExerciseListMapper.toScheduleEntity(request, user, exercise);
                })
                .collect(Collectors.toList());

        exerciseScheduleRepository.saveAll(entities);
    }
    public List<ExerciseScheduleResponse> getSchedulesByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        List<ExerciseSchedule> schedules = exerciseScheduleRepository.findByUser(user);
        return schedules.stream()
                .map(ExerciseListMapper::toScheduleResponseDto)
                .collect(Collectors.toList());
    }
}
