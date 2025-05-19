package com.motionmate.controller.goods;

import com.motionmate.dto.goods.order.OrderRequestDto;
import com.motionmate.dto.goods.order.OrderResponseDto;
import com.motionmate.global.oauth.CustomOAuth2User;
import com.motionmate.service.goods.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     */
    @PostMapping
    public OrderResponseDto createOrder(
            @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal CustomOAuth2User oauthUser
    ) {
        return orderService.createOrder(requestDto, oauthUser.getUser());
    }

    /**
     * 주문 목록 조회
     */
    @GetMapping
    public List<OrderResponseDto> getOrders(
            @AuthenticationPrincipal CustomOAuth2User oauthUser
    ) {
        return orderService.getOrders(oauthUser.getUser());
    }
}
