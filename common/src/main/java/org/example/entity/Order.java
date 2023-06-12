package org.example.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "\"order\"")
@NoArgsConstructor
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

    public Order(String orderName, String orderNumber, Long pointDiscountPrice, PaymentMethod paymentMethod,
        OrderCustomerType orderCustomerType, String deliveryMessage, String deliveryPhoneNumber, String receiver,
        String streetAddress, String detailAddress, String zipCode, String customerEmail, String customerName,
        String customerPhoneNumber, String customerPersonalCustomsCode, List<OrderItem> items, User customer,
        boolean isAddressToChargeAdditionalFee, AreaType areaType) {
        this.orderName = orderName;
        this.orderNumber = orderNumber;
        this.totalOriginalPrice = this.calculateTotalOriginalPrice(items);
        this.pointDiscountPrice = pointDiscountPrice;
        this.deliveryFee = this.calculateTotalDeliveryFee(items);
        this.paymentMethod = paymentMethod;
        this.orderCustomerType = orderCustomerType;
        this.deliveryMessage = deliveryMessage;
        this.deliveryPhoneNumber = deliveryPhoneNumber;
        this.receiver = receiver;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerPersonalCustomsCode = customerPersonalCustomsCode;
        this.items = items;
        this.customer = customer;

        if (isAddressToChargeAdditionalFee && areaType == AreaType.Jeju) {
            this.deliveryType = DeliveryType.JEJU;
        }

        if (isAddressToChargeAdditionalFee && areaType == AreaType.AreaExceptForJeju) {
            this.deliveryType = DeliveryType.ISLAND;
        }

        if (!isAddressToChargeAdditionalFee) {
            this.deliveryType = DeliveryType.NORMAL;
        }

        if (paymentMethod == PaymentMethod.VirtualAccount) {
            this.refundAccountBankName = customer.getBank().getBankName();
            this.refundBankAccount = customer.getBankAccount();
            this.refundAccountHolder = customer.getBankAccountHolder();
        }
    }

    private Long calculateTotalOriginalPrice(List<OrderItem> items) {
        return items.stream().mapToLong(OrderItem::getOrderItemTotalAmount).sum();
    }

    private Long calculateTotalDeliveryFee(List<OrderItem> items) {
        return items.stream().mapToLong(OrderItem::getDeliveryFee).sum();
    }

    public void applyCouponDiscountPrice(Long couponDiscountPrice) {
        this.couponDiscountPrice += couponDiscountPrice;
        this.totalDiscountPrice += couponDiscountPrice;
    }

    public Order copy(Order order) {
        Order newOrder = new Order();
        this.orderName = order.orderName;
        this.orderNumber = order.orderNumber;
        this.totalOriginalPrice = order.totalOriginalPrice;
        this.pointDiscountPrice = order.pointDiscountPrice;
        this.deliveryFee = order.deliveryFee;
        this.paymentMethod = order.paymentMethod;
        this.orderCustomerType = order.orderCustomerType;
        this.deliveryMessage = order.deliveryMessage;
        this.deliveryPhoneNumber = order.deliveryPhoneNumber;
        this.receiver = order.receiver;
        this.streetAddress = order.streetAddress;
        this.detailAddress = order.detailAddress;
        this.zipCode = order.zipCode;
        this.customerEmail = order.customerEmail;
        this.customerName = order.customerName;
        this.customerPhoneNumber = order.customerPhoneNumber;
        this.customerPersonalCustomsCode = order.customerPersonalCustomsCode;
        this.items = order.items;
        this.customer = order.customer;
        this.deliveryType = order.deliveryType;
        this.refundAccountBankName = order.refundAccountBankName;
        this.refundBankAccount = order.refundBankAccount;
        this.refundAccountHolder = order.refundAccountHolder;

        return newOrder;
    }
}
