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
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class ExerciseListService {

    private final ExerciseListRepository exerciseListRepository;
    private final ExerciseImageRepository exerciseImageRepository;
    private final S3ServiceUtils s3ServiceUtils;
    private final ExerciseScheduleRepository exerciseScheduleRepository;
    private final UserRepository userRepository;
    private final ExerciseCompletedRepository exerciseCompletedRepository;


    public ResponseEntity<?> createExerciseList(ExerciseListRequest request) {
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

    public List<ExerciseListWithImageResponse> getAllExerciseList() {
        List<ExerciseList> exercise = exerciseListRepository.findAll();
        return exercise.stream().map(exercises -> {
            ExerciseImage image = exerciseImageRepository.findFirstByExercise_ExerciseId(exercises.getExerciseId())
                    .orElse(null);

            SaveImageResponse imageDto = (image != null) ? ExerciseListMapper.fromExerciseImage(image) : null;

            return ExerciseListMapper.from(exercises, imageDto);
        }).collect(Collectors.toList());
    }

    public void createExerciseSchedule(List<ExerciseScheduleRequest> requests) {
        for (ExerciseScheduleRequest request : requests) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + request.getUserId()));
            ExerciseList exercise = exerciseListRepository.findById(request.getExerciseId())
                    .orElseThrow(() -> new RuntimeException("Exercise not found: " + request.getExerciseId()));

            // 🔥 여기서 max set_number 구함
            int maxSetNumber = exerciseScheduleRepository.findMaxSetNumber(
                    user.getId(), request.getDate(), exercise.getExerciseId()
            );

            // 요청에서 받은 setNumber는 무시하고 max+1로 부여
            ExerciseSchedule newSchedule = ExerciseSchedule.builder()
                    .user(user)
                    .exercise(exercise)
                    .date(request.getDate())
                    .setNumber(maxSetNumber + 1)
                    .kg(request.getKg())
                    .reps(request.getReps())
                    .time(request.getTime())
                    .build();

            exerciseScheduleRepository.save(newSchedule);
        }
    }

    public List<ExerciseScheduleResponse> getSchedulesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        List<ExerciseSchedule> schedules = exerciseScheduleRepository.findByUser(user);
        return schedules.stream()
                .map(ExerciseListMapper::toScheduleResponseDto)
                .collect(Collectors.toList());
    }

    public void saveCompletedExercise(List<ExerciseCompletedRequest> requestList) {
        for (ExerciseCompletedRequest request : requestList) {
            // userId로 User 객체 조회
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

            // 중복 조회
            Optional<ExerciseCompleted> existing = exerciseCompletedRepository
                    .findByUserAndDateAndExerciseCategoryAndExerciseNameAndSetNumber(
                            user,
                            request.getDate(),
                            request.getExerciseCategory(),
                            request.getExerciseName(),
                            request.getSetNumber()
                    );

            if (existing.isPresent()) {
                ExerciseCompleted entity = existing.get();
                // 기존 엔티티 업데이트
                entity.setKg(request.getKg());
                entity.setReps(request.getReps());
                entity.setTime(request.getTime());
                entity.setCompleted(request.isCompleted());
                exerciseCompletedRepository.save(entity);
            } else {
                ExerciseCompleted entity = ExerciseListMapper.toExerciseCompletedRequest(request, user);
                exerciseCompletedRepository.save(entity);
            }
        }
    }

    public List<ExerciseCompletedResponse> getCompletedExercisesByUserId(Long userId) {
        return exerciseCompletedRepository.findByUserId(userId)
                .stream()
                .map(ExerciseListMapper::fromExerciseCompletedResponse)  // 👈 정적 메서드로 호출
                .collect(Collectors.toList());
    }

    public void deleteCompletedExercises(List<CompletedExerciseDeleteRequest> deleteRequests) {
        for (CompletedExerciseDeleteRequest req : deleteRequests) {
            deleteCompletedExercise(
                    req.getUserId(),
                    req.getDate(),
                    req.getExerciseName(),
                    req.getExerciseCategory(),
                    req.getSetNumber()
            );
        }
    }

    public void deleteCompletedExercise(Long userId, String date, String name, String category, int setNumber) {
        exerciseCompletedRepository.deleteByUserIdAndDateAndExerciseNameAndExerciseCategoryAndSetNumber(
                userId, date, name, category, setNumber
        );
    }

    public void updateExerciseSchedules(List<ExerciseScheduleUpdateRequest> requests) {
        for (ExerciseScheduleUpdateRequest req : requests) {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

            ExerciseCategory category = ExerciseCategory.valueOf(req.getExerciseCategory());
            ExerciseSchedule schedule = exerciseScheduleRepository
                    .findByUser_IdAndDateAndExercise_NameAndExercise_CategoryAndSetNumber(
                            req.getUserId(), req.getDate(), req.getExerciseName(),
                            category, req.getSetNumber()
                    )
                    .orElseThrow(() -> new IllegalArgumentException("스케줄 데이터를 찾을 수 없습니다."));

            schedule.setKg(req.getKg());
            schedule.setReps(req.getReps());
            schedule.setTime(req.getTime());
            // 필요한 경우: scheduleRepository.save(schedule); ← @Transactional이면 생략 가능
        }
    }

    public void deleteExerciseSchedules(List<ExerciseScheduleDeleteRequest> deleteRequests) {
        for (ExerciseScheduleDeleteRequest req : deleteRequests) {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

            ExerciseCategory category = ExerciseCategory.valueOf(req.getExerciseCategory());

            // 1️⃣ 스케줄 삭제
            Optional<ExerciseSchedule> scheduleOpt = exerciseScheduleRepository
                    .findByUser_IdAndDateAndExercise_NameAndExercise_CategoryAndSetNumber(
                            req.getUserId(), req.getDate(), req.getExerciseName(), category, req.getSetNumber()
                    );
            scheduleOpt.ifPresent(exerciseScheduleRepository::delete);

            // 2️⃣ completed 테이블도 함께 삭제
            exerciseCompletedRepository.deleteByUserIdAndDateAndExerciseNameAndExerciseCategoryAndSetNumber(
                    req.getUserId(),
                    req.getDate(),
                    req.getExerciseName(),
                    req.getExerciseCategory(),
                    req.getSetNumber()
            );
        }
    }
    public void deleteScheduleWithCompleted(ExerciseScheduleSetDeleteRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        ExerciseCategory category = ExerciseCategory.valueOf(req.getExerciseCategory());

        // 1. schedule 삭제
        exerciseScheduleRepository.deleteByUser_IdAndDateAndExercise_NameAndExercise_CategoryAndSetNumber(
                req.getUserId(), req.getDate(), req.getExerciseName(), category, req.getSetNumber()
        );

        // 2. completed도 같이 삭제
        exerciseCompletedRepository.deleteByUserIdAndDateAndExerciseNameAndExerciseCategoryAndSetNumber(
                req.getUserId(), req.getDate(), req.getExerciseName(), req.getExerciseCategory(), req.getSetNumber()
        );
    }
}