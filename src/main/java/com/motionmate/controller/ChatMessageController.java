package com.motionmate.controller;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.service.ChatMessageService;
import com.motionmate.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload ChatMessageRequestDto dto,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        log.info("📨 WebSocket 수신: roomId={}, sender={}, message={}",
                roomId, dto.getSenderNickname(), dto.getMessage());

        User user = (User) sessionAttributes.get("user");

        if (dto.getType() == null) {
            throw new IllegalArgumentException("메시지 타입이 누락되었습니다.");
        }

        dto.setChatRoomId(roomId);
        dto.setSenderNickname(user.getProfile().getNickname());

        if (dto.getType() == ChatMessage.MessageType.ENTER) {
            // 입장 메시지만 즉시 저장 (선택사항)
            ChatRoom room = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
            chatMessageService.saveEnterMessage(room, user);
        }

        // TALK 등 일반 메시지는 저장하지 않고 Redis로만 전송
        log.info("📤 RedisPublisher.publish() 호출됨: {}", dto);
        redisPublisher.publish(dto);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Exception ex) {
        return "메시지 처리 중 오류 발생: " + ex.getMessage();
    }
}
