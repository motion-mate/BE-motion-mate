package com.motionmate.controller.chat;

import com.motionmate.mongo.ChatMessageDocument;
import com.motionmate.mongo.ChatMessageMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mongo/chat")
public class ChatMessageMongoController {

    private final ChatMessageMongoRepository chatMessageMongoRepository;

    // ✅ 채팅 메시지 조회용 API (테스트용)
    @GetMapping("/{roomId}")
    public List<ChatMessageDocument> getMessages(@PathVariable Long roomId) {
        return chatMessageMongoRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }
}

