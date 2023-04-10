package org.example.controller;

import org.example.domain.Order;
import org.example.dto.order.CreateMemberOrderInputDTO;
import org.example.dto.order.CreateMemberOrderOutputDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @PostMapping
    public CreateMemberOrderOutputDTO createOrder(CreateMemberOrderInputDTO createMemberOrderInputDto) {
        return new CreateMemberOrderOutputDTO(true, new Order(), false);
    }
}
