package com.motionmate.controller;

import com.motionmate.dto.exercise.ExerciseListRequest;
import com.motionmate.service.ExerciseListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseRecordController {
    private final ExerciseListService service;

    @PostMapping
    public ResponseEntity<?> createExerciseList(@RequestBody ExerciseListRequest request){
        return service.createExerciseList(request);
    }
    // POST /
    // GET /{id}
    // PUT /{id}
    // DELETE /{id}
}