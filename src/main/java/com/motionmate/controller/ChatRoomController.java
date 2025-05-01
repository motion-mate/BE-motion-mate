package com.motionmate.controller;

import com.motionmate.dto.chat.ChatRoomRequestDto;
import com.motionmate.dto.chat.ChatRoomResponseDto;
import com.motionmate.service.ChatRoomService;
import com.motionmate.global.oauth.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestBody @Valid ChatRoomRequestDto dto,
                                                              @AuthenticationPrincipal CustomOAuth2User user) {
        String username = user.getUser().getNickname();
        ChatRoomResponseDto response = chatRoomService.createChatRoom(dto, username);
        return ResponseEntity.ok(response);
    }

    // 전체 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<ChatRoomResponseDto>> getAllChatRooms() {
        return ResponseEntity.ok(chatRoomService.getAllChatRooms());
    }

    // 단일 채팅방 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatRoomService.getChatRoom(roomId));
    }

    // 채팅방 필터 적용
    @GetMapping("/filter")
    public ResponseEntity<List<ChatRoomResponseDto>> filterChatRooms(
            @RequestParam(required = false) String exerciseType,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String keyword
    ) {
        List<ChatRoomResponseDto> result = chatRoomService.filterChatRooms(exerciseType, address, date, keyword);
        return ResponseEntity.ok(result);
    }

    // 채팅방 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long roomId,
                                               @AuthenticationPrincipal CustomOAuth2User user) {
        String username = user.getUser().getNickname();
        chatRoomService.deleteChatRoomByCreator(roomId, username); // 생성자만 삭제 가능하도록 설계
        return ResponseEntity.noContent().build();
    }
}
