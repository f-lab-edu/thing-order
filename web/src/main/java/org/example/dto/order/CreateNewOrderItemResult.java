package org.example.dto.order;

import org.example.entity.OrderItem;

import lombok.Getter;

@Getter
public class CreateNewOrderItemResult {

    private final OrderItem orderItem;
    private final long productDiscountPrice;
    private final long deliveryFee;

    public CreateNewOrderItemResult(OrderItem orderItem, long productDiscountPrice,
                                    long deliveryFee) {
        this.orderItem = orderItem;
        this.productDiscountPrice = productDiscountPrice;
        this.deliveryFee = deliveryFee;
    }
}
