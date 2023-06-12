package org.example.dto.order;

import java.util.List;

import org.example.entity.OrderItem;

import lombok.Getter;

@Getter
public class NewOrderItemResult {

    private final List<OrderItem> orderItems;
    private final CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput;

    public NewOrderItemResult(List<OrderItem> orderItemList,
        CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput) {
        this.orderItems = orderItemList;
        this.checkAdditionalDeliveryFeeOutput = checkAdditionalDeliveryFeeOutput;
    }
}
