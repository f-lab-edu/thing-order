package org.example.resolver;

import org.example.dto.order.ConfirmMemberOrderResponseDto;
import org.example.dto.order.ConfirmOrderRequestDto;
import org.example.dto.order.CreateMemberOrderRequestDto;
import org.example.dto.order.CreateMemberOrderResponseDto;
import org.example.dto.order.GetMemberOrderByUserRequestDto;
import org.example.dto.order.GetMemberOrderByUserResponseDto;
import org.example.dto.order.JwtPayload;
import org.example.dto.order.OrderInfoResponse;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.service.JwtService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderResolver {

    private final OrderService orderService;
    private final UserService userService;
    private final JwtService jwtService;

    @MutationMapping
    public CreateMemberOrderResponseDto createMemberOrderV3(
        @Argument("input") CreateMemberOrderRequestDto requestDto,
        @ContextValue(name = "x-jwt") String jwtAccessToken) throws Exception {
        if (!jwtService.verifyToken(jwtAccessToken)) {
            throw new GraphqlException("403 FORBIDDEN");
        }

        JwtPayload jwtPayload = jwtService.getJwtPayload(jwtAccessToken);
        User user = userService.findUserById(jwtPayload.getId())
            .orElseThrow(() -> new Exception("user not found"));

        Order newOrder = orderService.createMemberOrder(requestDto.getPaymentMethod(), user,
            requestDto.getItems(), requestDto.getPointDiscountPrice(), requestDto.getDeliveryId(),
            requestDto.getDeliveryMessage());

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
