package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String orderName;

    private String orderNumber;

    private Long totalOriginalPrice;

    private Long totalDiscountPrice;

    private Long productDiscountPrice;

    private Long pointDiscountPrice;

    private Long couponDiscountPrice;

    private Long deliveryFee;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderCustomerType orderCustomerType;

    @Enumerated(EnumType.STRING)
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

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User customer;
}
