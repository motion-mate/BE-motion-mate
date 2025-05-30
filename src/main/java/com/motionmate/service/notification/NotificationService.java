package com.motionmate.service.notification;

import com.motionmate.domain.notification.Notification;
import com.motionmate.domain.notification.NotificationRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.notification.NotificationRequestDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.notification.NotificationMapper;
import com.motionmate.service.websocket.NotificationSocketSender;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSocketSender notificationSocketSender;
    private final UserRepository userRepository;

    // 알림 생성
    public Notification createNotification(NotificationRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        Notification notification = NotificationMapper.toEntity(requestDto, user);
        Notification savedNotification = notificationRepository.save(notification);

        notificationSocketSender.sendNotification(
                user.getId(),
                NotificationMapper.toDto(savedNotification)
        );

        return savedNotification;
    }

    // 특정 유저 알림 전체 조회
    @Transactional(readOnly = true)
    public List<Notification> getNotificationByUser(Long userId) {
        // User 객체에 해당하는 알림들을 생성일 기준으로 내림차순 정렬하여 반환
        // @AuthenticationPrincipal 어노테이션을 통해 컨트롤러에서 자동으로 User 객체 주입
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }


    // 알림 읽음처리
    public Notification markNotificationAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."));

        if(!notification.getUser().getId().equals(userId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "본인의 알림만 읽음처리 할 수 있습니다.");
        }
        notification.markAsRead();
        notificationRepository.save(notification);
        return notification;
    }

    // 전체 알림 읽음 처리
    public void markNotificationAsReadAll(Long userId) {
        int updatedCount = notificationRepository.markAllAsReadByUserId(userId);

        if (updatedCount == 0) {
            throw new CustomException(HttpStatus.NOT_FOUND, "읽지 않은 알림이 없습니다.");
        }
    }

    // 알림 삭제
    public void deleteNotification(Long notificationId) {
        if(!notificationRepository.existsById(notificationId)) {
            throw new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다.");
        }
        notificationRepository.deleteById(notificationId);
    }

    public void deleteAllNotification(Long userId) {
        int deletedCount = notificationRepository.deleteAllByUserId(userId);

        if (deletedCount == 0) {
            throw new CustomException(HttpStatus.NOT_FOUND, "삭제할 알림이 없습니다.");
        }
    }

}
