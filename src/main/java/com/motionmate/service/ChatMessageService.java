package com.motionmate.service;

import com.motionmate.domain.chat.ChatMessage;
import com.motionmate.domain.chat.ChatMessageRepository;
import com.motionmate.domain.chat.ChatRoom;
import com.motionmate.domain.chat.ChatRoomRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.chat.ChatMessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.motionmate.mapper.ChatMessageMapper.toEntity;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessage saveMessage(Long roomId, ChatMessageRequestDto dto) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        ChatMessage message = toEntity(chatRoom, sender, dto);
        return  chatMessageRepository.save(message);

    }

}
