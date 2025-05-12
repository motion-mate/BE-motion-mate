package com.motionmate.controller;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static com.motionmate.mapper.ChatMessageMapper.toDto;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatService;

    @MessageMapping("/chat/{roomId}") // 클라이언트 → /pub/chat/{roomId}
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload ChatMessageRequestDto dto,
                            @Header("simpSessionAttributes")Map<String, Object> sessionAttributes) {

        // WebSocket 세션에서 유저 아이디 추출
        Long userId = (Long) sessionAttributes.get("userId");

        // 메시지 저장 서비스 호출 → 저장 후 DTO 변환
        ChatMessage saved = chatService.saveMessage(roomId, dto, userId);
        ChatMessageResponseDto response = toDto(saved);

        // 해당 채팅방 구독자에게 브로드캐스트
        template.convertAndSend("/sub/chat/" + roomId, response);
    }

    // WebSocket 메시지 예외 처리
    @MessageExceptionHandler
    @SendToUser("/queue/errors") // 클라이언트는 이 경로 구독하고 있어야 함
    public String handleException(Exception ex) {

        return "메시지 처리 중 오류 발생: " + ex.getMessage();

    }

}
