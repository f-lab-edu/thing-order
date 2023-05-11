package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Order {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String orderNumber;
    private Long totalOriginalPrice;
    private Long totalDiscountPrice;
    private Long productDiscountPrice;
    private Long pointDiscountPrice;
    private Long couponDiscountPrice;
    private Long deliveryFee;
    private List<OrderItem> items;
    private User customer;
    private PaymentMethod paymentMethod;
    private OrderCustomerType orderCustomerType;
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
    private String orderName;
    private Payment payment;
    private String customerEmail;
    private String customerName;
    private String customerPhoneNumber;
    private String customerPersonalCustomsCode;
    private DeliveryType deliveryType;
}
