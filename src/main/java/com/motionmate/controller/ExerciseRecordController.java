package com.motionmate.controller;

import com.motionmate.dto.exercise.ExerciseListRequest;
import com.motionmate.dto.exercise.ExerciseListWithImageResponse;
import com.motionmate.dto.exercise.ExerciseScheduleRequest;
import com.motionmate.dto.exercise.ExerciseScheduleResponse;
import com.motionmate.service.ExerciseListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseRecordController {
    private final ExerciseListService service;

    @PostMapping
    public ResponseEntity<?> createExerciseList(@RequestBody ExerciseListRequest request){
        return service.createExerciseList(request);
    }
    @GetMapping("/list")
    public List<ExerciseListWithImageResponse> getAllExercise() {
        return service.getAllExerciseList();
    }
    @PostMapping("/schedule")
    public ResponseEntity<?> createExerciseSchedules(@RequestBody List<ExerciseScheduleRequest> requests) {
        service.createExerciseSchedule(requests);
        return ResponseEntity.ok("운동 스케줄이 등록되었습니다.");
    }
    @GetMapping("/schedule/{userId}")
    public ResponseEntity<List<ExerciseScheduleResponse>> getSchedules(@PathVariable("userId") Long userId){
        List<ExerciseScheduleResponse> response = service.getSchedulesByUserId(userId);
        return ResponseEntity.ok(response);
    }
    // POST /
    // GET /{id}
    // PUT /{id}
    // DELETE /{id}
}