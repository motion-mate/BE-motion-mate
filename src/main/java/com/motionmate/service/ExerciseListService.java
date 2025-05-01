package com.motionmate.service;


import com.motionmate.domain.exercise.ExerciseImageRepository;
import com.motionmate.domain.exercise.ExerciseList;
import com.motionmate.domain.exercise.ExerciseListRepository;
import com.motionmate.dto.exercise.ExerciseListRequest;
import com.motionmate.mapper.ExerciseListMapper;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExerciseListService {

    private final ExerciseListRepository exerciseListRepository;
    private final ExerciseImageRepository exerciseImageRepository;
    private final S3ServiceUtils s3ServiceUtils;

    public ResponseEntity<?> createExerciseList(ExerciseListRequest request){

        ExerciseList exerciseList = ExerciseListMapper.toExerciseList(request);

        ExerciseList saved = exerciseListRepository.save(exerciseList);
        exerciseImageRepository.saveAll(saved.getImageUrl());

        return ResponseEntity.ok("진짜 성공한거니? 운동등록완료");
    }
}
