package org.example.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.example.config.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@TypeDef(name = "psql_enum", typeClass = PostgreSQLEnumType.class)
@NoArgsConstructor
@ToString
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
    private Map<String, Object> options = new HashMap<>();

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

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "order_item_order_status_enum")
    @Type(type = "psql_enum")
    private OrderStatus orderStatus;

    @OneToOne
    private Shop shop;

    private Long deliveryFee;

    private Long baseShippingFee;

    private Long jejuShippingFee;

    private Long islandShippingFee;

    @Column(name = "original_jeju_shipping_fee")
    private Long originJejuShippingFee;

    @Column(name = "original_island_shipping_fee")
    private Long originIslandShippingFee;

    @Column(name = "original_base_shipping_fee")
    private Long originBaseShippingFee;

    private Long conditionalFreeDeliveryFeeStandardByShop;

    private boolean isAcceptedConditionalFreeDeliveryFee;

    private boolean isAcceptedConditionalFreeDeliveryFeeWhenOrder;

    private Long originalDeliveryFeeBeforeDeliveryDiscount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupons;

    public OrderItem(Product product, Long orderItemTotalPaymentAmount, Long orderQuantity,
        Long orderItemTotalAmount, Long productDiscountAmount, LocalDateTime orderStatusDate,
        OrderStatus orderStatus, Long deliveryFee, Long baseShippingFee, Long jejuShippingFee,
        Long islandShippingFee, Long originJejuShippingFee, Long originIslandShippingFee,
        Long originBaseShippingFee, Long conditionalFreeDeliveryFeeStandardByShop,
        boolean isAcceptedConditionalFreeDeliveryFee,
        Long originalDeliveryFeeBeforeDeliveryDiscount, Shop shop,
        ProductOption userSelectOption) {
        this.product = product;
        this.orderItemTotalPaymentAmount = orderItemTotalPaymentAmount;
        this.orderQuantity = orderQuantity;
        this.orderItemTotalAmount = orderItemTotalAmount;
        this.productDiscountAmount = productDiscountAmount;
        this.orderStatusDate = orderStatusDate;
        this.orderStatus = orderStatus;
        this.deliveryFee = deliveryFee;
        this.baseShippingFee = baseShippingFee;
        this.jejuShippingFee = jejuShippingFee;
        this.islandShippingFee = islandShippingFee;
        this.originJejuShippingFee = originJejuShippingFee;
        this.originIslandShippingFee = originIslandShippingFee;
        this.originBaseShippingFee = originBaseShippingFee;
        this.conditionalFreeDeliveryFeeStandardByShop = conditionalFreeDeliveryFeeStandardByShop;
        this.isAcceptedConditionalFreeDeliveryFee = isAcceptedConditionalFreeDeliveryFee;
        this.originalDeliveryFeeBeforeDeliveryDiscount = originalDeliveryFeeBeforeDeliveryDiscount;
        this.shop = shop;
        this.options = getOrderItemOption(userSelectOption, orderQuantity);
    }

    public Map<String, Object> getOrderItemOption(ProductOption productOption, long orderQuantity) {
        if (productOption != null) {
            return Map.of("optionId", productOption.getOptionId(),
                "optionName1", productOption.getOptionName1(),
                "optionValue1", productOption.getOptionValue1(),
                "optionName2", productOption.getOptionName2(),
                "optionValue2", productOption.getOptionValue2(),
                "optionName3", productOption.getOptionName3(),
                "optionValue3", productOption.getOptionValue3(),
                "optionPrice", productOption.getOptionPrice(),
                "orderQuantity", orderQuantity
            );
        } else {
            return Map.of("orderQuantity", orderQuantity);
        }
    }

    public void setCoupons(Coupon coupons) {
        this.coupons = coupons;
    }

    public void setBaseShippingFee(Long baseShippingFee) {
        this.baseShippingFee = baseShippingFee;
    }

    public void setJejuShippingFee(Long jejuShippingFee) {
        this.jejuShippingFee = jejuShippingFee;
    }

    public void setIslandShippingFee(Long islandShippingFee) {
        this.islandShippingFee = islandShippingFee;
    }

    public void setAcceptedConditionalFreeDeliveryFee(boolean acceptedConditionalFreeDeliveryFee) {
        isAcceptedConditionalFreeDeliveryFee = acceptedConditionalFreeDeliveryFee;
    }

    public void setAcceptedConditionalFreeDeliveryFeeWhenOrder(
        boolean acceptedConditionalFreeDeliveryFeeWhenOrder) {
        isAcceptedConditionalFreeDeliveryFeeWhenOrder = acceptedConditionalFreeDeliveryFeeWhenOrder;
    }

    public void setDeliveryFee(Long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
