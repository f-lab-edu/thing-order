package org.example.message;

import lombok.Getter;
import org.example.entity.OrderItem;

import java.util.List;

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
