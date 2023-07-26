package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.Order;

@Getter
@Setter
public class CreateMemberOrderResponseDto {

    private boolean ok;
    private OrderResponse results;
    private boolean isZeroPaidOrder;

    public CreateMemberOrderResponseDto(boolean ok, Order order, boolean isZeroPaidOrder) {
        this.ok = ok;
        this.results = this.toDto(order);
        this.isZeroPaidOrder = isZeroPaidOrder;
    }

    private OrderResponse toDto(Order order) {
        return new OrderResponse(order);
    }
}

