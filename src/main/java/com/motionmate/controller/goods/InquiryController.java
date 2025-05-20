package com.motionmate.controller.goods;

import com.motionmate.domain.goods.Inquiry;
import com.motionmate.domain.user.User;
import com.motionmate.dto.goods.inquiry.InquiryRequestDto;
import com.motionmate.dto.goods.inquiry.InquiryResponseDto;
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
    public ResponseEntity<Void> create(@AuthenticationPrincipal User user, @RequestBody @Valid InquiryRequestDto dto) {
        Inquiry inquiry = InquiryMapper.toEntity(dto, user);
        service.save(inquiry);
        return ResponseEntity.ok().build();
    }

    // 문의 목록 조회
    @GetMapping("/user")
    public List<InquiryResponseDto> getUserInquiries(@AuthenticationPrincipal User user) {
        return service.findByUser(user).stream()
                .map(InquiryMapper::toResponseListDto)
                .toList();
    }

    // 문의 상세 조회
    @GetMapping("/{id}")
    public InquiryResponseDto getById(@PathVariable Long id) {
        return InquiryMapper.toResponseDetailDto(service.findById(id));
    }
}
