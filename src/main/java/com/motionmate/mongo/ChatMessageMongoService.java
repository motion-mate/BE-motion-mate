package com.motionmate.mongo;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageMongoService {

    private final ChatMessageMongoRepository mongoRepository;

    public void saveToMongo(ChatMessage message) {
        ChatMessageDocument doc = ChatMessageDocument.builder()
                .roomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .senderNickname(message.getSender().getProfile().getNickname())
                .message(message.getMessage())
                .sentAt(message.getSentAt() != null ? message.getSentAt() : LocalDateTime.now())
                .type(message.getType().name())
                .build();

        mongoRepository.save(doc);
    }

    public List<ChatMessageDocument> getMessagesByRoomId(Long roomId) {
        return mongoRepository.findByRoomIdOrderBySentAtAsc(roomId);
    }

}
