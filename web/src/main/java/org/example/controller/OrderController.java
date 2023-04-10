package org.example.controller;

import org.example.domain.Order;
import org.example.dto.order.CreateMemberOrderInputDTO;
import org.example.dto.order.CreateMemberOrderOutputDTO;
import org.example.dto.order.GetOrderByUserOutputDTO;
import org.example.dto.order.OrderInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @PostMapping
    public CreateMemberOrderOutputDTO createOrder(CreateMemberOrderInputDTO createMemberOrderInputDto) {
        return new CreateMemberOrderOutputDTO(true, new Order(), false);
    }

    @GetMapping("/{userId}")
    public GetOrderByUserOutputDTO getOrdersByUser(@PathVariable() String userId) {
        return new GetOrderByUserOutputDTO(true, new OrderInfo());
    }
}
