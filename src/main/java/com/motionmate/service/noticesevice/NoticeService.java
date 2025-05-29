package com.motionmate.service.noticesevice;

import com.motionmate.domain.notice.Notice;
import com.motionmate.domain.notice.NoticeRepository;
import com.motionmate.dto.Notice.NoticeRequestDto;
import com.motionmate.dto.Notice.NoticeResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeResponseDto> getAllNotices(){
        return noticeRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public NoticeResponseDto getNotice(Long id){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을수 없습니다."));
        return toDto(notice);
    }

    @Transactional
    public NoticeResponseDto createNotice(NoticeRequestDto requestDto){
        Notice notice = Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
        return toDto(noticeRepository.save(notice));
    }

    private NoticeResponseDto toDto(Notice notice){
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
