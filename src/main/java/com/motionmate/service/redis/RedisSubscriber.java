package com.motionmate.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfileRepository;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.global.exception.ChatMessageNotFoundException;
import com.motionmate.mapper.chat.ChatMessageMapper;
import com.motionmate.service.chat.ChatMessageService;
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
            // 1. Redis 메시지 디코딩
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            if (publishMessage == null) throw new ChatMessageNotFoundException();

            log.info("📩 RedisSubscriber 수신: {}", publishMessage);

            // 2. JSON 문자열 이중 escape 처리 해제
            if (publishMessage.startsWith("\"") && publishMessage.endsWith("\"")) {
                publishMessage = publishMessage.substring(1, publishMessage.length() - 1)
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\");
            }

            // 3. 역직렬화
            ChatMessageRequestDto roomMessage = objectMapper.readValue(publishMessage, ChatMessageRequestDto.class);
            System.out.println("<<메세지>>"+roomMessage);
            // 4. 분기 처리
            if (roomMessage.getType() == ChatMessage.MessageType.TALK) {
                ChatRoom chatRoom = chatRoomRepository.findById(roomMessage.getChatRoomId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

                User sender = userRepository.findUserByNickname(roomMessage.getSenderNickname())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

                // 메시지 저장
                ChatMessage savedMessage = chatMessageService.saveMessage(chatRoom.getId(), roomMessage, sender);
                ChatMessageResponseDto response = ChatMessageMapper.toDto(savedMessage);

                // STOMP 전송
                messagingTemplate.convertAndSend(
                        "/sub/chat/room/" + roomMessage.getChatRoomId(),
                        response
                );

                // Redis에도 저장
                redisTemplate.opsForList().rightPush(
                        "CHAT_MESSAGES:" + roomMessage.getChatRoomId(),
                        objectMapper.writeValueAsString(response)
                );
            }else if(roomMessage.getType() == ChatMessage.MessageType.ENTER) {
                // STOMP 전송
                messagingTemplate.convertAndSend(
                        "/sub/chat/room/" + roomMessage.getChatRoomId(),
                        ChatMessageResponseDto.builder()
                                .message(roomMessage.getMessage())
                                .type(roomMessage.getType())
                                .senderNickname(roomMessage.getSenderNickname())
                                .build()
                );
            }else {
                ChatRoom chatRoom = chatRoomRepository.findById(roomMessage.getChatRoomId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

                User sender = userRepository.findUserByNickname(roomMessage.getSenderNickname())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

                String redisKey = "CHAT_ROOM_MEMBERS:" + roomMessage.getChatRoomId();

                if (roomMessage.getType() == ChatMessage.MessageType.ENTER) {
                    // Redis Set에 이미 포함된 유저인지 확인
                    Boolean alreadyEntered = redisTemplate.opsForSet().isMember(redisKey, sender.getProfile().getNickname());

                    if (Boolean.TRUE.equals(alreadyEntered)) {
                        log.info("⚠️ 이미 입장한 사용자입니다. 중복 ENTER 무시: {}", sender.getProfile().getNickname());
                        return; // 중복 입장 -> 저장/전송하지 않음
                    }

                    // Redis Set에 유저 추가
                    redisTemplate.opsForSet().add(redisKey, sender.getProfile().getNickname());
                }

                if (roomMessage.getType() == ChatMessage.MessageType.EXIT) {
                    // 퇴장 시 Redis에서 유저 제거
                    redisTemplate.opsForSet().remove(redisKey, sender.getProfile().getNickname());
                }

                // 메시지 저장
                ChatMessage savedMessage = chatMessageService.saveMessage(chatRoom.getId(), roomMessage, sender);
                ChatMessageResponseDto response = ChatMessageMapper.toDto(savedMessage);

                // STOMP 전송
                messagingTemplate.convertAndSend(
                        "/sub/chat/room/" + roomMessage.getChatRoomId(),
                        response
                );

                // Redis에도 저장
                redisTemplate.opsForList().rightPush(
                        "CHAT_MESSAGES:" + roomMessage.getChatRoomId(),
                        objectMapper.writeValueAsString(response)
                );
            }


        } catch (Exception e) {
            log.error("❌ RedisSubscriber 메시지 처리 실패", e);
            throw new ChatMessageNotFoundException();
        }
    }
}
