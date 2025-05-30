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
    private final ChatMessageMongoService chatMessageMongoService; // ✅ 추가


    public ChatMessage saveMessage(Long roomId, ChatMessageRequestDto dto, User sender) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        if (dto.getType() == ChatMessage.MessageType.ENTER) {
            // ✅ 이미 방에 participant가 없는 경우에만 시스템 메시지 저장
            boolean exists = chatRoomParticipantRepository.existsByChatRoomAndUser(chatRoom, sender);
            if (exists) return null; // 멤버에 존재하면 메시지 생략
        }

        ChatMessage message = ChatMessageMapper.toEntity(chatRoom, sender, dto);
        ChatMessage saved = chatMessageRepository.save(message);

        chatMessageMongoService.saveToMongo(saved); // ✅ MongoDB에도 저장
        return saved;
    }

    public ChatMessage saveEnterMessage(ChatRoom room, User user) {
        Optional<ChatRoomParticipant> participantOpt =
                chatRoomParticipantRepository.findByChatRoomAndUser(room, user);

        // 존재하지 않으면 최초 입장: 참가자 등록 + 메시지 생성
        if (participantOpt.isEmpty()) {
            chatRoomParticipantRepository.save(new ChatRoomParticipant(room, user));

            ChatMessage message = new ChatMessage(room, user, "입장했습니다.", ChatMessage.MessageType.ENTER);

            ChatMessage saved = chatMessageRepository.save(message);
            chatMessageMongoService.saveToMongo(saved); // ✅ Mongo에도 저장
            return saved;
        }

        // 이미 참가자인 경우
        ChatRoomParticipant participant = participantOpt.get();

        // 이미 접속 중이면 메시지 생성 X
        if (participant.isConnected()) {
            return null;
        }

        // 이전에 나갔다가 다시 접속한 경우: reconnect()만
        participant.reconnect();
        chatRoomParticipantRepository.save(participant);

        // 메시지는 보내지 않음
        return null;
    }


    public List<ChatMessageResponseDto> getMessages(Long roomId) {
        List<ChatMessageDocument> docs = chatMessageMongoService.getMessagesByRoomId(roomId);

        return docs.stream()
                .map(ChatMessageMapper::fromDocument)
                .toList();
    }


    public void save(Long roomId, ChatMessageRequestDto request) {
        System.out.println(">>>"+request);
        System.out.println("roomId:>>>"+roomId);
        chatMessageRepository.save(ChatMessage.builder()
                        .message(request.getMessage())
                        .type(ChatMessage.MessageType.TALK)
                        .chatRoom(ChatRoom.builder().id(roomId).build())
                        .sender(userRepository.findByProfile_nickname(request.getSenderNickname()).orElseThrow())
                .build());
    }
}
