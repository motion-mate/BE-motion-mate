package com.motionmate.controller.chat;

import com.motionmate.domain.chat.*;
import com.motionmate.domain.user.User;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import com.motionmate.service.chat.ChatMessageService;
import com.motionmate.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisPublisher redisPublisher;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload ChatMessageRequestDto dto,
                            @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) {

        log.info("📨 WebSocket 수신: roomId={}, sender={}, message={}",
                roomId, dto.getSenderNickname(), dto.getMessage());

        //채팅방 구성원에게 메시지를 뿌려라

        User user = (User) sessionAttributes.get("user");

        if (dto.getType() == null) {
            throw new IllegalArgumentException("메시지 타입이 누락되었습니다.");
        }

        dto.setChatRoomId(roomId);
        dto.setSenderNickname(user.getProfile().getNickname());
        /*
        if (dto.getType() == ChatMessage.MessageType.ENTER) {
            // 입장 메시지만 즉시 저장 (선택사항)
            ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

            /*
            Optional<ChatRoomParticipant> participantOpt =
                    chatRoomParticipantRepository.findByChatRoom_idAndUser(roomId, user);

            if (participantOpt.isEmpty()) {
                log.warn("❗ ChatRoomParticipant 조회 실패: user={}, room={}", user.getId(), roomId);
            } else {
                ChatRoomParticipant participant = participantOpt.get();
                log.info("✅ ChatRoomParticipant 조회 성공: user={}, connected={}, room={}",
                        user.getId(), participant.isConnected(), roomId);

                participant.reconnect(); // connected = true
                chatRoomParticipantRepository.save(participant);
            }



            // chatMessageService.saveEnterMessage(room, user);
        }
        //*/


        // TALK 등 일반 메시지는 저장하지 않고 Redis로만 전송
        log.info("📤 RedisPublisher.publish() 호출됨: {}", dto);
        redisPublisher.publish(dto);
    }

    @PostMapping("/api/chatrooms/{roomId}/messages")
    public void sendTalkMessage(@PathVariable("roomId") Long roomId, @RequestBody ChatMessageRequestDto request) {
        //System.out.println("request>>>>:"+request);
        chatMessageService.save(roomId,request);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Exception ex) {
        return "메시지 처리 중 오류 발생: " + ex.getMessage();
    }
}
