package com.motionmate.service.chat;

import com.motionmate.domain.chat.*;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.mapper.chat.ChatMessageMapper;
import com.motionmate.mongo.ChatMessageDocument;
import com.motionmate.mongo.ChatMessageMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageMongoService chatMessageMongoService;

    // WebSocket 메시지 수신 시 호출 - TALK 메시지는 RDB와 MongoDB 모두에 저장

    public ChatMessage saveMessage(Long roomId, ChatMessageRequestDto dto, User sender) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        // 중복 입장 방지
        if (dto.getType() == ChatMessage.MessageType.ENTER) {
            boolean exists = chatRoomParticipantRepository.existsByChatRoomAndUser(chatRoom, sender);
            if (exists) return null;
        }

        ChatMessage message = ChatMessageMapper.toEntity(chatRoom, sender, dto);
        ChatMessage saved = chatMessageRepository.save(message);

        // TALK 메시지일 때 MongoDB에도 저장
        if (dto.getType() == ChatMessage.MessageType.TALK) {
            chatMessageMongoService.saveToMongo(saved);
        }

        return saved;
    }

    // 채팅방 최초 입장 처리

    public ChatMessage saveEnterMessage(ChatRoom room, User user) {
        Optional<ChatRoomParticipant> participantOpt =
                chatRoomParticipantRepository.findByChatRoomAndUserWithAll(room, user);

        if (participantOpt.isEmpty()) {
            chatRoomParticipantRepository.save(new ChatRoomParticipant(room, user));
            ChatMessage message = new ChatMessage(room, user, "입장했습니다.", ChatMessage.MessageType.ENTER);
            return chatMessageRepository.save(message);
        }

        ChatRoomParticipant participant = participantOpt.get();

        if (participant.isConnected()) {
            return null;
        }

        participant.reconnect();
        chatRoomParticipantRepository.save(participant);
        return null;
    }

    // 채팅 메시지 목록 조회 - MongoDB에서 TALK 메시지만
    public List<ChatMessageResponseDto> getMessages(Long roomId) {
        List<ChatMessageDocument> docs = chatMessageMongoService.getMessagesByRoomId(roomId);
        return docs.stream()
                .map(ChatMessageMapper::fromDocument)
                .toList();
    }

    public void save(Long roomId, ChatMessageRequestDto request) {
        ChatMessage message = ChatMessage.builder()
                .message(request.getMessage())
                .type(ChatMessage.MessageType.TALK)
                .chatRoom(ChatRoom.builder().id(roomId).build())
                .sender(userRepository.findByProfile_nickname(request.getSenderNickname())
                        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다.")))
                .build();

        chatMessageRepository.save(message);
        chatMessageMongoService.saveToMongo(message); // Mongo 저장도 함께 처리
    }
}
