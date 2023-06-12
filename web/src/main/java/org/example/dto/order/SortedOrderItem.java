package org.example.dto.order;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.OrderItem;
import org.example.entity.Shop;

@Getter
@Setter
public class SortedOrderItem {

    private List<OrderItem> orderItems;
    private Shop shop;
    private OrderItem maxOriginJejuShippingFeeItem;
    private OrderItem maxOriginBaseShippingFeeItem;
    private OrderItem maxOriginIslandShippingFeeItem;

    public SortedOrderItem(Shop shop) {
        this.orderItems = new ArrayList<>();
        this.shop = shop;
        this.maxOriginJejuShippingFeeItem = null;
        this.maxOriginBaseShippingFeeItem = null;
        this.maxOriginIslandShippingFeeItem = null;
    }
}
