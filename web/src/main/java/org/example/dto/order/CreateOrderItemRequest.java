package org.example.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateOrderItemRequest {

    private Long productId;
    private Long userCouponId;
    private Long optionId;
    private Long orderQuantity;

    public CreateOrderItemRequest(Long productId, Long userCouponId, Long optionId,
        Long orderQuantity) {
        this.productId = productId;
        this.userCouponId = userCouponId;
        this.optionId = optionId;
        this.orderQuantity = orderQuantity;
    }
}
