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

    // ✅ 채팅방 생성 (nickname 기반)
    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto, String creatorNickname) {
        User creator = userRepository.findByNickname(creatorNickname)
                .orElseThrow(() -> new EntityNotFoundException("해당 닉네임의 사용자가 존재하지 않습니다."));

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

    // ✅ 전체 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomResponseDto::new)
                .toList();
    }

    // ✅ 단일 채팅방 조회
    @Transactional(readOnly = true)
    public ChatRoomResponseDto getChatRoom(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        return new ChatRoomResponseDto(room);
    }

    // ✅ 채팅방 삭제 (생성자 nickname 일치 시에만 허용)
    @Transactional
    public void deleteChatRoomByCreator(Long roomId, String nickname) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 채팅방이 존재하지 않습니다."));

        if (!room.getCreator().getNickname().equals(nickname)) {
            throw new SecurityException("채팅방 생성자만 삭제할 수 있습니다.");
        }

        chatRoomRepository.deleteById(roomId);
    }
}
