package org.example.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Type(type = "json")
    @Column(columnDefinition = "json")
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

    @OneToOne
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupons;

    public void setIsAcceptedConditionalFreeDeliveryFee(boolean isAcceptedConditionalFreeDeliveryFee) {
        this.isAcceptedConditionalFreeDeliveryFee = isAcceptedConditionalFreeDeliveryFee;
    }

    public void setIsAcceptedConditionalFreeDeliveryFeeWhenOrder(boolean acceptedConditionalFreeDeliveryFeeWhenOrder) {
        isAcceptedConditionalFreeDeliveryFeeWhenOrder = acceptedConditionalFreeDeliveryFeeWhenOrder;
    }

    public OrderItemOption getOrderItemOption(ProductOption productOption, long orderQuantity) {
        if (productOption != null) {
            OrderItemOption orderItemOption = new OrderItemOption();
            orderItemOption.setOptionId(productOption.getOptionId());
            orderItemOption.setOptionName1(productOption.getOptionName1());
            orderItemOption.setOptionValue2(productOption.getOptionValue2());
            orderItemOption.setOptionName2(productOption.getOptionName2());
            orderItemOption.setOptionValue3(productOption.getOptionValue3());
            orderItemOption.setOptionName3(productOption.getOptionName3());
            orderItemOption.setOptionValue3(productOption.getOptionValue3());
            orderItemOption.setOptionPrice(productOption.getOptionPrice());
            orderItemOption.setOrderQuality(orderQuantity);

            return orderItemOption;
        } else {
            OrderItemOption orderItemOption = new OrderItemOption();
            orderItemOption.setOrderQuality(orderQuantity);

            return orderItemOption;
        }
    }
}
