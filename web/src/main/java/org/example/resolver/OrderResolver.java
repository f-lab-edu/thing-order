package org.example.resolver;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateMemberOrderRequestDto;
import org.example.dto.CreateMemberOrderResponseDto;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.message.JwtPayload;
import org.example.service.JwtService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

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
        if (jwtAccessToken != null && !this.jwtService.verifyToken(jwtAccessToken)) {
            throw new GraphqlException("403 FORBIDDEN");
        }

        JwtPayload jwtPayload = this.jwtService.getJwtPayload(jwtAccessToken);
        User user = this.userService.findUserById(jwtPayload.getId())
                .orElseThrow(() -> new Exception("user not found"));

        Order newOrder = this.orderService.createMemberOrder(requestDto.getPaymentMethod(), user,
                requestDto.getItems(), requestDto.getPointDiscountPrice(), requestDto.getDeliveryId(),
                requestDto.getDeliveryMessage());

        if (newOrder.isZeroPaid()) {
            return new CreateMemberOrderResponseDto(true, newOrder, true);
        }

        return new CreateMemberOrderResponseDto(true, newOrder, false);
    }

    @QueryMapping
    public String healthCheck() {
        return "hello";
    }
}
