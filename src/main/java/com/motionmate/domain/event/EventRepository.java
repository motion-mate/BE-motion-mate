package com.motionmate.domain.event;

import com.motionmate.domain.event.Event;
import com.motionmate.domain.event.Event.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 이벤트 타입으로 조회
    List<Event> findByType(EventType type);

    // 활성화된 이벤트만 조회
    List<Event> findByActiveTrue();

    // 타입 + 활성 상태로 조회
    List<Event> findByTypeAndActiveTrue(EventType type);
}