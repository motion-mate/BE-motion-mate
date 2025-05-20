package com.motionmate.service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHAT_MESSAGE_PREFIX = "CHAT_MESSAGES:";


    // Redis에 채팅 메시지를 저장
    public void saveMessage(ChatMessageRequestDto dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            log.info("✅ Redis 발행 JSON: {}", json);
            String key = CHAT_MESSAGE_PREFIX + dto.getChatRoomId();
            redisTemplate.opsForList().rightPush(key, json);
        } catch (Exception e) {
            throw new RuntimeException("Redis에 메시지 저장 실패", e);
        }
    }


    // 해당 채팅방의 모든 메시지를 호출
    public List<ChatMessageRequestDto> getMessages(Long chatRoomId) {
        String key = CHAT_MESSAGE_PREFIX + chatRoomId;
        List<String> jsonList = redisTemplate.opsForList().range(key, 0, -1);

        return jsonList.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, ChatMessageRequestDto.class);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(msg -> msg != null)
                .collect(Collectors.toList());
    }


    // 최근 N개의 메시지를 호출
    public List<ChatMessageRequestDto> getRecentMessages(Long chatRoomId, int count) {
        String key = CHAT_MESSAGE_PREFIX + chatRoomId;
        List<String> jsonList = redisTemplate.opsForList().range(key, -count, -1);

        return jsonList.stream()
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, ChatMessageRequestDto.class);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(msg -> msg != null)
                .collect(Collectors.toList());
    }


    // 메시지 모두 삭제 (테스트 용도)
    public void clearMessages(Long chatRoomId) {
        redisTemplate.delete(CHAT_MESSAGE_PREFIX + chatRoomId);
    }
}
