package com.motionmate.service.event;

import com.motionmate.domain.event.Event;
import com.motionmate.domain.event.EventParticipation;
import com.motionmate.domain.event.EventParticipationRepository;
import com.motionmate.domain.event.EventRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.event.EventRequestDto;
import com.motionmate.dto.event.EventResponseDto;
import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.event.EventMapper;
import com.motionmate.mapper.s3.S3FileMapper;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final EventParticipationRepository participationRepository;
    private final S3ServiceUtils s3ServiceUtils;
    private final int dummyUserPk = 0;

    // ✅ 1. 이벤트 생성
    public Event createEvent(EventRequestDto dto) {
        S3FileRequest imageInfo = null;

        if (dto.getBucketKey() != null && !dto.getBucketKey().isBlank()) {
            imageInfo = new S3FileRequest(dto.getImageUrl(), dto.getBucketKey(), dto.getOrgName());

            // 이동 및 삭제 처리
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(imageInfo, dummyUserPk);
            imageInfo = S3FileMapper.toS3FileRequest(moved);
            s3ServiceUtils.deleteUserTempFiles(dummyUserPk);
        }

        Event event = EventMapper.toEntity(dto, imageInfo);
        event.updateActiveStatus();

        return eventRepository.save(event);
    }

    // ✅ 2. 이벤트 참여
    @Transactional
    public void participate(User user, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));

        if (!event.isActive()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이벤트 비활성화됨");
        }

        // ✅ 공통으로 재고 확인
        if (event.getStock() <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이벤트 재고가 소진되었습니다.");
        }

        // ✅ 타입 구분 없이 재고 차감
        event.decreaseStock();

        // ✅ 참여 저장
        participationRepository.save(new EventParticipation(user, event));

        // ✅ 재고 0 되면 자동 종료 처리
        if (event.getStock() == 0) {
            event.deactivate();
        }
    }

    // ✅ 3. 전체 이벤트 조회
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventMapper::toDto)
                .toList();
    }

    // ✅ 4. 단일 이벤트 조회
    @Transactional(readOnly = true)
    public EventResponseDto getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));
        return EventMapper.toDto(event);
    }

    // ✅ 5. 이벤트 수정
    public void updateEvent(Long id, EventRequestDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));

        String oldBucketKey = event.getBucketKey();
        String newBucketKey = dto.getBucketKey();

        boolean isNewImageUploaded = newBucketKey != null
                && !newBucketKey.isBlank()
                && !newBucketKey.equals(oldBucketKey);

        boolean isImageDeleted = newBucketKey != null && newBucketKey.isBlank();

        String finalImageUrl = event.getImageUrl();
        String finalBucketKey = event.getBucketKey();
        String finalOrgName = event.getOrgName();
        // 이미지 삭제 요청
        if (isImageDeleted) {
            if (oldBucketKey != null && !oldBucketKey.isBlank()) {
                s3ServiceUtils.deleteFile(oldBucketKey);
            }
            finalImageUrl = null;
            finalBucketKey = null;
            finalOrgName = null;
        }  // 새 이미지가 업로드된 경우
        else if (isNewImageUploaded) {
            if (oldBucketKey != null && !oldBucketKey.isBlank()) {
                s3ServiceUtils.deleteFile(oldBucketKey);
            }

            S3FileRequest s3FileRequest = new S3FileRequest(dto.getImageUrl(), newBucketKey, dto.getOrgName());
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(s3FileRequest, 0);

            finalImageUrl = moved.url();
            finalBucketKey = moved.bucketKey();
            finalOrgName = moved.orgName();

            s3ServiceUtils.deleteUserTempFiles(0); // 선택
        }

        // 최종 이벤트 정보 업데이트
        event.update(
                dto.getTitle(),
                dto.getDescription(),
                finalImageUrl,
                finalOrgName,
                finalBucketKey,
                dto.getType(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getStock()
        );

        event.updateActiveStatus();
    }

    // ✅ 6. 이벤트 숨김 처리 (soft delete)
    public void deactivateEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));
        event.deactivate(); // active = false
    }

    public void hideEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));
        event.hide();
    }

    public void unhideEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이벤트 없음"));
        event.unhide();
    }
}
