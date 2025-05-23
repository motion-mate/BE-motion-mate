package com.motionmate.domain.event;

import com.motionmate.domain.event.EventParticipation;
import com.motionmate.domain.user.User;
import com.motionmate.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {

    // 한 유저가 해당 이벤트에 참여했는지 여부 (중복 방지용)
    Optional<EventParticipation> findByUserAndEvent(User user, Event event);

    // 해당 이벤트의 참여자 목록
    List<EventParticipation> findByEvent(Event event);

    // 유저가 참여한 모든 이벤트
    List<EventParticipation> findByUser(User user);
}
