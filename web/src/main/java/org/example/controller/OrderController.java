package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.domain.Order;
import org.example.dto.order.ConfirmMemberOrderResponseDto;
import org.example.dto.order.ConfirmOrderRequestDto;
import org.example.dto.order.CreateMemberOrderRequestDto;
import org.example.dto.order.CreateMemberOrderResponseDto;
import org.example.dto.order.GetOrderByUserOutputDto;
import org.example.dto.order.OrderInfoResponse;

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
