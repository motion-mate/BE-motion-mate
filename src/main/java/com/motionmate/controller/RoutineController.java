package com.motionmate.controller;

import com.motionmate.domain.exercise.ExerciseSchedule;
import com.motionmate.domain.exercise.ExerciseScheduleRepository;
import com.motionmate.dto.exercise.ExerciseScheduleRequest;
import com.motionmate.dto.routine.*;
import com.motionmate.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;
    private final ExerciseScheduleRepository exerciseScheduleRepository;

    @PostMapping
    public ResponseEntity<?> createRoutine(@RequestBody RoutineCreateRequest request) {
        try {
            routineService.createRoutine(request.getUserId(), request);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping("/list")
    public List<RoutineGroupResponse> getRoutines(@RequestParam("userId") Long userId) {
        return routineService.getRoutinesByUser(userId);
    }
    @DeleteMapping
    public ResponseEntity<?> deleteRoutine(@RequestBody RoutineDeleteRequest request) {
        routineService.deleteRoutineByTitleAndUserId(request.getUserId(), request.getTitle());
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/set")
    public ResponseEntity<String> deleteRoutineSet(@RequestBody RoutineDeleteSetRequest request) {
        routineService.deleteRoutineSet(request.getUserId(),
                request.getTitle(),
                request.getExerciseName(),
                request.getExerciseCategory(),
                request.getSetNumber());
        return ResponseEntity.ok("세트 삭제 완료");
    }
    @PutMapping("/title")
    public ResponseEntity<String> updateRoutineTitle(@RequestBody RoutineTitleUpdateRequest request) {
        routineService.updateRoutineTitle(request);
        return ResponseEntity.ok("루틴 제목 수정 완료");
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateRoutine(@RequestBody RoutineUpdateRequest request) {
        routineService.updateRoutine(request);
        return ResponseEntity.ok("루틴 수정 완료");
    }
    @PostMapping("/from-routine")
    public ResponseEntity<?> registerRoutineToSchedule(@RequestBody List<ExerciseScheduleRequest> list) {
        try {
            routineService.registerRoutineSchedule(list);
            return ResponseEntity.ok("루틴 일정 등록 완료!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}