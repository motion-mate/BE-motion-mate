package com.motionmate.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, String> {
    List<ChatMessageDocument> findByRoomIdOrderBySentAtAsc(Long roomId);
}
