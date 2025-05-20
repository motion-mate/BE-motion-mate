package com.motionmate.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfileRepository;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.global.exception.ChatMessageNotFoundException;
import com.motionmate.mapper.ChatMessageMapper;
import com.motionmate.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final UserProfileRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 1. 문자열 디코딩
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            if (publishMessage == null) {
                throw new ChatMessageNotFoundException();
            }

            log.info("📩 RedisSubscriber 수신: {}", publishMessage);

            // 2. JSON 문자열인지 확인하고 이스케이프된 경우 처리
            // 메시지가 이중으로 감싸져 있으면 디코딩
            if (publishMessage.startsWith("\"") && publishMessage.endsWith("\"")) {
                publishMessage = publishMessage.substring(1, publishMessage.length() - 1)
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\");
            }

            // 3. 역직렬화
            ChatMessageRequestDto roomMessage = objectMapper.readValue(publishMessage, ChatMessageRequestDto.class);

            if (roomMessage.getType().equals(ChatMessage.MessageType.TALK)) {
                ChatRoom chatRoom = chatRoomRepository.findById(roomMessage.getChatRoomId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

                User sender = userRepository.findUserByNickname(roomMessage.getSenderNickname())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

                ChatMessage savedMessage = chatMessageService.saveMessage(
                        chatRoom.getId(), roomMessage, sender
                );

                ChatMessageResponseDto response = ChatMessageMapper.toDto(savedMessage);
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getChatRoomId(), response);

                redisTemplate.opsForList().rightPush("CHAT_MESSAGES:" + roomMessage.getChatRoomId(), publishMessage);
            }

        } catch (Exception e) {
            log.error("❌ RedisSubscriber 메시지 처리 실패", e);
            throw new ChatMessageNotFoundException();
        }
    }
}
