package com.motionmate.controller.goods;

import com.motionmate.domain.goods.Inquiry;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.inquiry.InquiryAnswerRequestDto;
import com.motionmate.dto.goods.inquiry.InquiryAnswerResponseDto;
import com.motionmate.dto.goods.inquiry.InquiryRequestDto;
import com.motionmate.dto.goods.inquiry.InquiryResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.mapper.goods.InquiryMapper;
import com.motionmate.service.goods.InquiryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryService service;

    public InquiryController(InquiryService service) {
        this.service = service;
    }

    // 문의 등록
    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal CustomOAuth2User oauthUser,
                                       @RequestBody @Valid InquiryRequestDto dto) {
        User user = oauthUser.getUser();
        Inquiry inquiry = InquiryMapper.toEntity(dto, user);
        service.save(inquiry);
        return ResponseEntity.ok().build();
    }

    // 문의 목록 조회
    @GetMapping("/user")
    public List<InquiryResponseDto> getUserInquiries(@AuthenticationPrincipal CustomOAuth2User oauthUser) {
        User user = oauthUser.getUser();
        return service.findByUser(user).stream()
                .map(InquiryMapper::toResponseListDto)
                .toList();
    }

    // 문의 상세 조회
    @GetMapping("/{id}")
    public InquiryResponseDto getById(@PathVariable Long id) {
        return InquiryMapper.toResponseDetailDto(service.findById(id));
    }

    // 문의 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id,
                                              @AuthenticationPrincipal CustomOAuth2User oauthUser) {
        User user = oauthUser.getUser();
        service.deleteInquiry(id, user);
        return ResponseEntity.noContent().build();
    }

    // 답변 등록
    @PutMapping("/{id}/answer")
    public ResponseEntity<InquiryAnswerResponseDto> answerInquiry(@PathVariable Long id,
                                                                  @RequestBody @Valid InquiryAnswerRequestDto dto,
                                                                  @AuthenticationPrincipal CustomOAuth2User oauthUser) {
        User user = oauthUser.getUser();
        Inquiry updated = service.answerInquiry(id, dto.getAnswer(), user);
        InquiryAnswerResponseDto response = InquiryMapper.toAnswerResponseDto(updated);
        return ResponseEntity.ok(response);
    }
}
