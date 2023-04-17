package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderItem {
    private Product product;
    private OrderItemOption options;
    private Long orderItemVat;
    private DeliveryCompany deliveryCompany;
    private String deliveryNumber;
    private Long orderItemTotalPaymentAmount;
    private Long orderQuantity;
    private Long orderItemTotalAmount;
    private Long pointDiscountAmount;
    private Long couponDiscountAmount;
    private Long productDiscountAmount;
    private String cancelReason;
    private String refundReason;
    private LocalDateTime deliveryReleaseDate;
    private LocalDateTime receiptCompletedDate;
    private LocalDateTime orderStatusDate;
    private OrderStatus orderStatus;
    private Shop shop;
    private Long deliveryFee;
    private Long baseShippingFee;
    private Long jejuShippingFee;
    private Long islandShippingFee;
    private Long originJejuShippingFee;
    private Long originIslandShippingFee;
    private Long originBaseShippingFee;
    private Long conditionalFreeDeliveryFeeStandardByShop;
    private boolean isAcceptedConditionalFreeDeliveryFee;
    private boolean isAcceptedConditionalFreeDeliveryFeeWhenOrder;
    private Long originalDeliveryFeeBeforeDeliveryDiscount;
}
