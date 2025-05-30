package com.motionmate.controller.exercise;

import com.motionmate.dto.exercise.*;
import com.motionmate.service.exercise.ExerciseListService;
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
    @PostMapping("/completed")
        public ResponseEntity<Void> saveCompletedExercises(@RequestBody List<ExerciseCompletedRequest> requestList){

        service.saveCompletedExercise(requestList);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/completed/{userId}")
        public List<ExerciseCompletedResponse> getCompletedExercises(@PathVariable("userId") Long userId){
        return service.getCompletedExercisesByUserId(userId);
    }
    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteCompletedExercises(
            @RequestBody List<CompletedExerciseDeleteRequest> deleteRequests) {
        System.out.println("요청 들어옴: " + deleteRequests.size());
        deleteRequests.forEach(System.out::println);
        service.deleteCompletedExercises(deleteRequests); // 단일 호출로 위임
        return ResponseEntity.ok().build();
    }
    @PutMapping("/schedule")
    public ResponseEntity<Void> updateSchedules(
            @RequestBody List<ExerciseScheduleUpdateRequest> requests) {
        service.updateExerciseSchedules(requests);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/schedule/bulk")
    public ResponseEntity<Void> deleteExerciseSchedules(@RequestBody List<ExerciseScheduleDeleteRequest> deleteRequests) {
        service.deleteExerciseSchedules(deleteRequests);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/schedule")
    public ResponseEntity<Void> deleteSchedule(@RequestBody ExerciseScheduleSetDeleteRequest req){
        service.deleteScheduleWithCompleted(req);
        return ResponseEntity.ok().build();
    }
    // POST /
    // GET /{id}
    // PUT /{id}
    // DELETE /{id}
}