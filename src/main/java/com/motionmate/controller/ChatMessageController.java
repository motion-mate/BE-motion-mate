package com.motionmate.controller;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.mapper.ChatMessageMapper;
import com.motionmate.service.ChatMessageService;
import com.motionmate.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.motionmate.mapper.ChatMessageMapper.toDto;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;

    // WebSocket 메시지 처리
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload ChatMessageRequestDto dto,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {
        User user = (User) sessionAttributes.get("user");

        if (dto.getType() == null) {
            throw new IllegalArgumentException("메시지 타입이 누락되었습니다.");
        }

        ChatMessage saved = null;

        if (dto.getType() == ChatMessage.MessageType.ENTER) {
            ChatRoom room = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

            saved = chatMessageService.saveEnterMessage(room, user);
        } else {
            saved = chatMessageService.saveMessage(roomId, dto, user);
        }

        // 메시지가 실제 저장된 경우에만 브로드캐스트
        if (saved != null) {
            ChatMessageResponseDto response = ChatMessageMapper.toDto(saved);
            template.convertAndSend("/sub/chat/" + roomId, response);
        }
    }


    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Exception ex) {
        return "메시지 처리 중 오류 발생: " + ex.getMessage();
    }

}
