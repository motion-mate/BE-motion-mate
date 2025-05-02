package com.motionmate.service;

import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfileRepository;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.chat.ChatRoomRequestDto;
import com.motionmate.dto.chat.ChatRoomResponseDto;
import com.motionmate.dto.chat.ChatRoomUpdateDto;
import com.motionmate.mapper.ChatRoomMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserProfileRepository userProfileRepository;

    // 채팅방 생성 (nickname 기반)
    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto, String creatorNickname) {
        User creator = userProfileRepository.findUserByNickname(creatorNickname)
                .orElseThrow(() -> new EntityNotFoundException("해당 닉네임의 사용자가 존재하지 않습니다."));

        ChatRoom chatRoom = ChatRoomMapper.toEntity(dto, creator);
        ChatRoom saved = chatRoomRepository.save(chatRoom);
        return ChatRoomMapper.toDto(saved);
    }

    // 전체 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomMapper::toDto)
                .toList();
    }

    // 단일 채팅방 조회
    @Transactional(readOnly = true)
    public ChatRoomResponseDto getChatRoom(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        return ChatRoomMapper.toDto(room);
    }

    // 필터 조건 기반 채팅방 검색
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> filterChatRooms(String type, String address, String dateStr, String keyword) {
        ChatRoom.ExerciseType exerciseType = (type != null && !type.isBlank())
                ? ChatRoom.ExerciseType.valueOf(type.toUpperCase())
                : null;

        LocalDate date = (dateStr != null && !dateStr.isBlank())
                ? LocalDate.parse(dateStr)
                : null;

        List<ChatRoom> filteredRooms = chatRoomRepository.filterBy(exerciseType, address, date, keyword);

        return filteredRooms.stream()
                .map(ChatRoomMapper::toDto)
                .toList();
    }

    // 채팅방 수정 (생성자 nickname 일치 시에만 허용)
    @Transactional
    public ChatRoomResponseDto updateChatRoom(Long roomId, String nickname, ChatRoomUpdateDto dto) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        if (!room.getCreator().getProfile().getNickname().equals(nickname)) {
            throw new SecurityException("채팅방 생성자만 수정할 수 있습니다.");
        }

        if (dto.getTitle() != null) room.updateTitle(dto.getTitle());
        if (dto.getExerciseType() != null) room.updateExerciseType(dto.getExerciseType());
        if (dto.getAddress() != null) room.updateAddress(dto.getAddress());
        if (dto.getLatitude() != null) room.updateLatitude(dto.getLatitude());
        if (dto.getLongitude() != null) room.updateLongitude(dto.getLongitude());
        if (dto.getPromiseDate() != null) room.updatePromiseDate(dto.getPromiseDate());
        if (dto.getPromiseTime() != null) room.updatePromiseTime(dto.getPromiseTime());

        return ChatRoomMapper.toDto(room);
    }
    // 채팅방 삭제 (생성자 nickname 일치 시에만 허용)
    @Transactional
    public void deleteChatRoomByCreator(Long roomId, String nickname) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 채팅방이 존재하지 않습니다."));

        if (!room.getCreator().getProfile().getNickname().equals(nickname)) {
            throw new SecurityException("채팅방 생성자만 삭제할 수 있습니다.");
        }

        chatRoomRepository.deleteById(roomId);
    }
}
