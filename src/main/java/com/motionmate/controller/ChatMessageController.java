package com.motionmate.controller;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import static com.motionmate.mapper.ChatMessageMapper.toDto;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatService;

    @MessageMapping("/chat/{roomId}") // 클라이언트 → /pub/chat/{roomId}
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequestDto dto) {
        // 메시지 저장 서비스 호출 → 저장 후 DTO 변환
        System.out.println("컨트롤러 진입!");
        System.out.println("roomId = " + roomId);
        System.out.println("DTO = " + dto);
        System.out.println("senderId = " +dto.getSenderId());
        ChatMessage saved = chatService.saveMessage(roomId, dto);
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
