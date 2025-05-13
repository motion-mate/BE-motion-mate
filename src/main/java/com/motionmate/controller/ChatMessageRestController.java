package com.motionmate.controller;

import com.motionmate.dto.chat.ChatMessageResponseDto;
import com.motionmate.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatrooms")
public class ChatMessageRestController {

    private final ChatMessageService chatService;

    @GetMapping("/{roomId}/messages")
    public List<ChatMessageResponseDto> getMessages(@PathVariable Long roomId) {
        return chatService.getMessages(roomId);
    }
}
