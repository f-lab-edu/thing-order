package org.example.controller;

import org.example.domain.Order;
import org.example.dto.order.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @PostMapping
    public CreateMemberOrderResponseDto createOrder(CreateMemberOrderRequestDto createMemberOrderRequestDto) {
        return new CreateMemberOrderResponseDto(true, new Order(), false);
    }

    @GetMapping("/{userId}")
    public GetOrderByUserOutputDto getOrdersByUser(@PathVariable() String userId) {
        return new GetOrderByUserOutputDto(true, new OrderInfoResponse());
    }

    @PostMapping("/confirm")
    public ConfirmMemberOrderResponseDto confirmOrder(ConfirmOrderRequestDto confirmOrderRequestDTO) {
        return new ConfirmMemberOrderResponseDto(true, true);
    }
}
