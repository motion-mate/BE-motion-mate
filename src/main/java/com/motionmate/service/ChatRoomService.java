package com.motionmate.service;

import com.motionmate.domain.chat.*;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserProfileRepository;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.chat.ChatRoomMemberDto;
import com.motionmate.dto.chat.ChatRoomRequestDto;
import com.motionmate.dto.chat.ChatRoomResponseDto;
import com.motionmate.dto.chat.ChatRoomUpdateDto;
import com.motionmate.mapper.ChatRoomMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserProfileRepository userProfileRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성 (nickname 기반)
    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto dto, String creatorNickname) {
        User creator = userProfileRepository.findUserByNickname(creatorNickname)
                .orElseThrow(() -> new EntityNotFoundException("해당 닉네임의 사용자가 존재하지 않습니다."));

        ChatRoom chatRoom = ChatRoomMapper.toEntity(dto, creator);
        ChatRoom saved = chatRoomRepository.save(chatRoom);

        chatRoomParticipantRepository.save(new ChatRoomParticipant(saved, creator));

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
    
    // 사용자가 참가한 채팅방 검색
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getChatRoomsByParticipant(String nickname) {
        User user = userProfileRepository.findUserByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        List<ChatRoomParticipant> participations = chatRoomParticipantRepository.findByUser(user);

        return participations.stream()
                .map(ChatRoomParticipant::getChatRoom)
                .map(ChatRoomMapper::toDto)
                .toList();
    }


    // 필터 조건 기반 채팅방 검색
    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> filterChatRooms(String type, String roadAddress, String dateStr, String keyword) {
        ChatRoom.ExerciseType exerciseType = (type != null && !type.isBlank())
                ? ChatRoom.ExerciseType.valueOf(type.toUpperCase())
                : null;

        LocalDate date = (dateStr != null && !dateStr.isBlank())
                ? LocalDate.parse(dateStr)
                : null;

        List<ChatRoom> filteredRooms = chatRoomRepository.filterBy(exerciseType, roadAddress, date, keyword);

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


        return ChatRoomMapper.toDto(room);
    }

    // 채팅방 입장
    @Transactional
    public void enterRoom(Long roomId, String nickname) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        User user = userProfileRepository.findUserByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        Optional<ChatRoomParticipant> existing = chatRoomParticipantRepository.findByChatRoomAndUser(room, user);

        if (existing.isPresent()) {
            existing.get().reconnect();
            return;
        }

        try {
            chatRoomParticipantRepository.save(new ChatRoomParticipant(room, user));
            // chatMessageRepository.save(new ChatMessage(room, user, "입장했습니다.", ChatMessage.MessageType.ENTER));
        } catch (DataIntegrityViolationException ex) {
            log.debug("⚠️ 중복 참가 삽입 시도 감지됨 - 무시 처리", ex);
            // 예외 후 세션 flush 시도 방지
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    // 채팅방 탈퇴
    @Transactional
    public void exitRoom(Long roomId, String nickname) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        User user = userProfileRepository.findUserByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        ChatRoomParticipant participant = chatRoomParticipantRepository.findByChatRoomAndUser(room, user)
                .orElseThrow(() -> new EntityNotFoundException("채팅방 참가 정보가 없습니다."));

        // ✅ 현재 유저가 방장일 경우 → 방장 위임 시도
        if (room.getCreator().getId().equals(user.getId())) {
            // 자신 제외하고 남은 참가자 중 한 명을 새로운 방장으로 설정
            List<ChatRoomParticipant> otherParticipants =
                    chatRoomParticipantRepository.findByChatRoom(room).stream()
                            .filter(p -> !p.getUser().getId().equals(user.getId()))
                            .toList();

            if (otherParticipants.isEmpty()) {
                // ✅ 참가자 없으면 방 삭제
                chatMessageRepository.deleteByChatRoomId(roomId);
                chatRoomParticipantRepository.delete(participant);
                chatRoomRepository.delete(room);
                return;
            } else {
                // ✅ 방장 위임
                User newCreator = otherParticipants.get(0).getUser();
                room.setCreator(newCreator); // 엔티티에 setter가 있어야 함
            }
        }

        // ✅ 일반 탈퇴 로직
        chatRoomParticipantRepository.delete(participant);
    }


    // 채팅방 나가기
    @Transactional
    public void disconnectFromRoom(Long roomId, String nickname) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));
        User user = userProfileRepository.findUserByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        ChatRoomParticipant participant = chatRoomParticipantRepository.findByChatRoomAndUser(room, user)
                .orElseThrow(() -> new EntityNotFoundException("채팅방 참가 정보가 없습니다."));

        participant.disconnect();
        chatRoomParticipantRepository.save(participant); // 반드시 저장!
    }


    // 전체 멤버 조회
    @Transactional(readOnly = true)
    public List<ChatRoomMemberDto> getParticipants(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        List<ChatRoomParticipant> participants = chatRoomParticipantRepository.findByChatRoom(room);

        return participants.stream()
                .map(p -> ChatRoomMemberDto.builder()
                        .userId(p.getUser().getId())
                        .nickname(p.getUser().getProfile().getNickname())
                        .connected(p.isConnected())
                        .build())
                .toList();
    }

    // 채팅방 삭제 (생성자 nickname 일치 시에만 허용)
    @Transactional
    public void deleteChatRoomByCreator(Long roomId, String nickname) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("삭제할 채팅방이 존재하지 않습니다."));

        if (!room.getCreator().getProfile().getNickname().equals(nickname)) {
            throw new SecurityException("채팅방 생성자만 삭제할 수 있습니다.");
        }

        // ✅ 1. 메시지 먼저 삭제
        chatMessageRepository.deleteByChatRoomId(roomId);

        // ✅ 2. 참가자 삭제
        chatRoomParticipantRepository.deleteByChatRoomId(roomId);

        // ✅ 3. 채팅방 삭제
        chatRoomRepository.deleteById(roomId);
    }


}
