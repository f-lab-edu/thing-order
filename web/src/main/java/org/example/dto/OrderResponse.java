package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.DeliveryType;
import org.example.entity.Order;
import org.example.entity.OrderCustomerType;
import org.example.entity.PaymentMethod;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    private long id;

    private String orderName;

    private String orderNumber;

    private Long totalOriginalPrice;

    private Long totalDiscountPrice;

    private Long productDiscountPrice;

    private Long pointDiscountPrice;

    private Long couponDiscountPrice;

    private Long deliveryFee;

    private PaymentMethod paymentMethod;

    private OrderCustomerType orderCustomerType;

    private DeliveryType deliveryType;

    private String refundBankAccount;

    private String refundAccountBankName;

    private String refundAccountHolder;

    private String deliveryMessage;

    private String deliveryPhoneNumber;

    private String receiver;

    private String streetAddress;

    private String detailAddress;

    private String zipCode;

    private LocalDateTime paymentDate;

    private String customerEmail;

    private String customerName;

    private String customerPhoneNumber;

    private String customerPersonalCustomsCode;


    public OrderResponse(Order order) {
        this.id = order.getId();
        this.orderName = order.getOrderName();
        this.orderNumber = order.getOrderNumber();
        this.totalOriginalPrice = order.getTotalOriginalPrice();
        this.totalDiscountPrice = order.getTotalDiscountPrice();
        this.productDiscountPrice = order.getProductDiscountPrice();
        this.pointDiscountPrice = order.getPointDiscountPrice();
        this.couponDiscountPrice = order.getCouponDiscountPrice();
        this.deliveryFee = order.getDeliveryFee();
        this.paymentMethod = order.getPaymentMethod();
        this.orderCustomerType = order.getOrderCustomerType();
        this.deliveryType = order.getDeliveryType();
        this.refundBankAccount = order.getRefundBankAccount();
        this.refundAccountBankName = order.getRefundAccountBankName();
        this.refundAccountHolder = order.getRefundAccountHolder();
        this.deliveryMessage = order.getDeliveryMessage();
        this.deliveryPhoneNumber = order.getDeliveryPhoneNumber();
        this.receiver = order.getReceiver();
        this.streetAddress = order.getStreetAddress();
        this.detailAddress = order.getDetailAddress();
        this.paymentDate = order.getPaymentDate();
        this.customerEmail = order.getCustomerEmail();
        this.customerName = order.getCustomerName();
        this.customerPhoneNumber = order.getCustomerPhoneNumber();
        this.customerPersonalCustomsCode = order.getCustomerPersonalCustomsCode();
    }
}
