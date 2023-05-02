package org.example.resolver;

import org.example.domain.Order;
import org.example.dto.order.ConfirmMemberOrderResponseDto;
import org.example.dto.order.ConfirmOrderRequestDto;
import org.example.dto.order.CreateMemberOrderResponseDto;
import org.example.dto.order.GetMemberOrderByUserRequestDto;
import org.example.dto.order.GetMemberOrderByUserResponseDto;
import org.example.dto.order.OrderInfoResponse;
import org.example.service.OrderService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class OrderResolver {
    private final OrderService orderService;

    public OrderResolver(OrderService orderService) {
        this.orderService = orderService;
    }

    @MutationMapping
    public CreateMemberOrderResponseDto createMemberOrderV3() {
        Order newOrder = orderService.createMemberOrder();
        return new CreateMemberOrderResponseDto(true, newOrder, false);
    }

    @QueryMapping
    public GetMemberOrderByUserResponseDto order(@Argument("input") GetMemberOrderByUserRequestDto getMemberOrderByUserRequestDto) {
        return new GetMemberOrderByUserResponseDto(true, new OrderInfoResponse());
    }

    @MutationMapping
    public ConfirmMemberOrderResponseDto confirmMemberOrder(ConfirmOrderRequestDto confirmOrderRequestDTO) {
        return new ConfirmMemberOrderResponseDto(true, true);
    }
}
