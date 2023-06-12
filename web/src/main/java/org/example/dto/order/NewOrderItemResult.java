package org.example.dto.order;

import org.example.entity.OrderItem;

import java.util.List;

import lombok.Getter;

@Getter
public class NewOrderItemResult {

    private final List<OrderItem> orderItems;
    private final Long totalProductDiscountPrice;
    private final Long totalDeliveryFee;

    public NewOrderItemResult(List<OrderItem> orderItemList, Long totalProductDiscountPrice,
                              Long totalDeliveryFee) {
        this.orderItems = orderItemList;
        this.totalProductDiscountPrice = totalProductDiscountPrice;
        this.totalDeliveryFee = totalDeliveryFee;
    }
}
