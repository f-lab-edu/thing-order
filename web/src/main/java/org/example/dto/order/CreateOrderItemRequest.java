package org.example.dto.order;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderItemRequest {
    private Long productId;
    private Long userCouponId;
    private Long optionId;
    private Long orderQuantity;
}
