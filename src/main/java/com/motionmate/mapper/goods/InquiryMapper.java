package com.motionmate.mapper.goods;

import com.motionmate.domain.goods.Inquiry;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.inquiry.InquiryAnswerResponseDto;
import com.motionmate.dto.goods.inquiry.InquiryRequestDto;
import com.motionmate.dto.goods.inquiry.InquiryResponseDto;


import java.time.LocalDateTime;

public class InquiryMapper {

    // 문의 등록용 Entity 생성
    public static Inquiry toEntity(InquiryRequestDto dto, User user) {
        return Inquiry.builder()
                .title(dto.getTitle())
                .category(dto.getCategory())
                .content(dto.getContent())
                .status(Inquiry.InquiryStatus.BEFORE) // ✅ 정확하게 매칭
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }

    // 목록용 응답 DTO
    public static InquiryResponseDto toResponseListDto(Inquiry i) {
        return InquiryResponseDto.builder()
                .id(i.getId())
                .title(i.getTitle())
                .category(i.getCategory())
                .date(i.getCreatedAt().toLocalDate().toString().replace("-", "."))
                .status(i.getStatus())
                .build();
    }

    // 상세용 응답 DTO(답변 포함)
    public static InquiryResponseDto toResponseDetailDto(Inquiry i) {
        return InquiryResponseDto.builder()
                .id(i.getId())
                .title(i.getTitle())
                .category(i.getCategory())
                .date(i.getCreatedAt().toLocalDate().toString().replace("-", "."))
                .status(i.getStatus())
                .content(i.getContent())
                .answer(i.getAnswer())
                .answeredAt(i.getAnsweredAt())
                .build();
    }

    public static InquiryAnswerResponseDto toAnswerResponseDto(Inquiry i){
        return InquiryAnswerResponseDto.builder()
                .id(i.getId())
                .title(i.getTitle())
                .category(i.getCategory())
                .content(i.getContent())
                .answer(i.getAnswer())
                .answeredAt(i.getAnsweredAt())
                .build();
    }
}
