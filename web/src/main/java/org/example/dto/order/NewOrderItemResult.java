package org.example.dto.order;

import java.util.List;

import org.example.entity.OrderItem;

import lombok.Getter;

@Getter
public class NewOrderItemResult {

    private final List<OrderItem> orderItems;
    private final Long totalProductDiscountPrice;
    private final Long totalDeliveryFee;
    private final CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput;

    public NewOrderItemResult(List<OrderItem> orderItemList, Long totalProductDiscountPrice, Long totalDeliveryFee,
        CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput) {
        this.orderItems = orderItemList;
        this.totalProductDiscountPrice = totalProductDiscountPrice;
        this.totalDeliveryFee = totalDeliveryFee;
        this.checkAdditionalDeliveryFeeOutput = checkAdditionalDeliveryFeeOutput;
    }
}
