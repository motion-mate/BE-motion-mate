package com.motionmate.service.routine;

import com.motionmate.domain.exercise.*;
import com.motionmate.domain.routine.Routine;
import com.motionmate.domain.routine.RoutineRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.exercise.ExerciseScheduleRequest;
import com.motionmate.dto.routine.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final ExerciseListRepository exerciseListRepository;
    private final UserRepository userRepository;
    private final ExerciseScheduleRepository exerciseScheduleRepository;

    private String normalizeTitle(String input) {
        return input.replaceAll("\\s+", "").toLowerCase();
    }
    @Transactional
    public void createRoutine(Long userId, RoutineCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("❌ 존재하지 않는 유저입니다."));
        String newTitle = request.getTitle();
        String normalizedNewTitle = normalizeTitle(newTitle);

        List<Routine> existingRoutines = routineRepository.findByUserId(userId);
        boolean isDuplicate = existingRoutines.stream()
                .map(r -> normalizeTitle(r.getTitle()))
                .anyMatch(normalized -> normalized.equals(normalizedNewTitle));

        if (isDuplicate) {
            throw new IllegalStateException("❌ 이미 존재하는 루틴 제목입니다.");
        }

        List<RoutineRequestDto> exercises = request.getExercises();

        for (RoutineRequestDto dto : exercises) {
            ExerciseList exercise = exerciseListRepository.findById(dto.getExerciseId())
                    .orElseThrow(() -> new IllegalArgumentException("❌ 존재하지 않는 운동입니다."));

            Routine routine = Routine.builder()
                    .user(user)
                    .title(request.getTitle())
                    .exercise(exercise)
                    .setNumber(dto.getSetNumber())
                    .kg(dto.getKg())
                    .reps(dto.getReps())
                    .time(dto.getTime())
                    .build();

            routineRepository.save(routine);
        }
    }
    @Transactional(readOnly = true)
    public List<RoutineGroupResponse> getRoutinesByUser(Long userId) {
        List<Routine> routines = routineRepository.findByUserId(userId);

        Map<String, List<Routine>> grouped = routines.stream()
                .collect(Collectors.groupingBy(Routine::getTitle));

        return grouped.entrySet().stream()
                .map(entry -> new RoutineGroupResponse(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(RoutineResponseDto::new)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteRoutineByTitleAndUserId(Long userId, String title) {
        routineRepository.deleteByUserIdAndTitle(userId, title);
    }
    @Transactional
    public void deleteRoutineSet(Long userId, String title, String exerciseName, String categoryString, int setNumber) {
        ExerciseCategory category = ExerciseCategory.valueOf(categoryString);
        routineRepository.deleteByUserIdAndTitleAndExercise_NameAndExercise_CategoryAndSetNumber(
                userId, title, exerciseName, category, setNumber
        );
    }
    @Transactional
    public void updateRoutineTitle(RoutineTitleUpdateRequest request) {
        List<Routine> routines = routineRepository.findByUserIdAndTitle(request.getUserId(), request.getOldTitle());
        for (Routine routine : routines) {
            routine.setTitle(request.getNewTitle());
        }
        routineRepository.saveAll(routines);
    }
    @Transactional
    public void updateRoutine(RoutineUpdateRequest request) {
        for (RoutineUpdateRequest.ExerciseDto dto : request.getExercises()) {
            ExerciseList exercise = exerciseListRepository
                    .findByNameAndCategory(dto.getExerciseName(), dto.getExerciseCategory())
                    .orElseThrow(() -> new RuntimeException("운동 정보 없음"));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("유저 없음"));

            Routine routine = Routine.builder()
                    .user(user)
                    .title(request.getTitle())
                    .exercise(exercise)
                    .setNumber(dto.getSetNumber())
                    .kg(dto.getKg() != null ? dto.getKg() : 0)
                    .reps(dto.getReps() != null ? dto.getReps() : 0)
                    .time(dto.getTime())
                    .build();

            routineRepository.save(routine);
        }
    }
    @Transactional
    public void registerRoutineSchedule(List<ExerciseScheduleRequest> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("등록할 데이터가 없습니다.");
        }

        Long userId = list.get(0).getUserId(); // 모든 항목은 동일한 userId, date라고 가정
        String date = list.get(0).getDate();


        // ✅ [1] 중복 검사: 같은 유저+날짜+운동이 이미 존재하는지 확인
        List<Long> incomingExerciseIds = list.stream()
                .map(ExerciseScheduleRequest::getExerciseId)
                .distinct()
                .toList();

        List<ExerciseSchedule> existingSchedules = exerciseScheduleRepository.findByUserIdAndDate(userId, date);

        for (ExerciseSchedule existing : existingSchedules) {
            if (incomingExerciseIds.contains(existing.getExercise().getExerciseId())) {
                throw new IllegalStateException("해당 날짜에 이미 등록된 운동이 있습니다: " +
                        existing.getExercise().getName());
            }
        }

        // ✅ [2] 저장
        for (ExerciseScheduleRequest req : list) {
            ExerciseSchedule entity = new ExerciseSchedule();
            entity.setUser(userRepository.getReferenceById(req.getUserId()));
            entity.setExercise(exerciseListRepository.getReferenceById(req.getExerciseId()));
            entity.setSetNumber(req.getSetNumber());
            entity.setKg(req.getKg());
            entity.setReps(req.getReps());
            entity.setTime(req.getTime());
            entity.setDate(req.getDate());
            exerciseScheduleRepository.save(entity);
        }
    }
}