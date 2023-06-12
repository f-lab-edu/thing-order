package org.example.dto.order;

import java.util.List;
import lombok.Getter;
import org.example.entity.OrderItem;

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
