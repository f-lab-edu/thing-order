package org.example.resolver;

import java.util.List;

import org.example.dto.order.ConfirmMemberOrderResponseDto;
import org.example.dto.order.ConfirmOrderRequestDto;
import org.example.dto.order.CreateMemberOrderResponseDto;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.dto.order.GetMemberOrderByUserRequestDto;
import org.example.dto.order.GetMemberOrderByUserResponseDto;
import org.example.dto.order.OrderInfoResponse;
import org.example.entity.Order;
import org.example.entity.PaymentMethod;
import org.example.entity.User;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderResolver {

    private final OrderService orderService;
    private final UserService userService;

    @MutationMapping
    public CreateMemberOrderResponseDto createMemberOrderV3() throws Exception {
        User user = userService.findUserById(88293L)
            .orElseThrow(() -> new Exception("user not found"));
        List<CreateOrderItemRequest> createOrderItemRequests = List.of(
            new CreateOrderItemRequest(70627111L, null, 1L, 1L));

        Long pointDiscountPrice = 0L;
        Long deliveryId = 5872L;
        String deliveryMessage = "temp delivery message";

        Order newOrder = orderService.createMemberOrder(PaymentMethod.Card, user,
            createOrderItemRequests, pointDiscountPrice, deliveryId, deliveryMessage);
        return new CreateMemberOrderResponseDto(true, newOrder, false);
    }

    @QueryMapping
    public GetMemberOrderByUserResponseDto order(
        @Argument("input") GetMemberOrderByUserRequestDto getMemberOrderByUserRequestDto) {
        return new GetMemberOrderByUserResponseDto(true, new OrderInfoResponse());
    }

    @MutationMapping
    public ConfirmMemberOrderResponseDto confirmMemberOrder(
        ConfirmOrderRequestDto confirmOrderRequestDTO) {
        return new ConfirmMemberOrderResponseDto(true, true);
    }
}
