package com.motionmate.service;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.chat.ChatRoomRequestDto;
import com.motionmate.dto.chat.ChatRoomResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅방 생성
    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto) {
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));

        ChatRoom chatRoom = new ChatRoom(
                dto.getTitle(),
                dto.getExerciseType(),
                dto.getAddress(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getPromiseDate(),
                dto.getPromiseTime(),
                creator
        );

        ChatRoom saved = chatRoomRepository.save(chatRoom);
        return new ChatRoomResponseDto(saved);
    }

    // 전체 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomResponseDto::new)
                .toList();
    }

    // ID로 단일 채팅방 조회
    @Transactional(readOnly = true)
    public ChatRoomResponseDto getChatRoom(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        return new ChatRoomResponseDto(room);
    }

    // 채팅방 삭제
    @Transactional
    public void deleteChatRoom(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("삭제할 채팅방이 존재하지 않습니다.");
        }
        chatRoomRepository.deleteById(roomId);
    }
}
