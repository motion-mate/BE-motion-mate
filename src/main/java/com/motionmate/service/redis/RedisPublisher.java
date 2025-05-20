package com.motionmate.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChannelTopic channelTopic; // ex: "chatroom"

    public void publish(ChatMessageRequestDto dto) {
        try {
            String messageJson = objectMapper.writeValueAsString(dto);
            redisTemplate.convertAndSend(channelTopic.getTopic(), messageJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis publish 실패", e);
        }
    }
}
