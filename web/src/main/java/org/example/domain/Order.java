package org.example.domain;

import java.time.LocalDateTime;
import java.util.List;

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
    private Product product;
    private Shop shop;
    private Long orderItemVat;
    private PaymentMethod paymentMethod;
    private OrderCustomerType orderCustomerType;
    private String refundAccountBankName;
    private String refundAccountHolder;
    private String deliveryMessage;
    private String deliverMessage;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getTotalOriginalPrice() {
        return totalOriginalPrice;
    }

    public void setTotalOriginalPrice(Long totalOriginalPrice) {
        this.totalOriginalPrice = totalOriginalPrice;
    }

    public Long getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public void setTotalDiscountPrice(Long totalDiscountPrice) {
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public Long getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(Long productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }

    public Long getPointDiscountPrice() {
        return pointDiscountPrice;
    }

    public void setPointDiscountPrice(Long pointDiscountPrice) {
        this.pointDiscountPrice = pointDiscountPrice;
    }

    public Long getCouponDiscountPrice() {
        return couponDiscountPrice;
    }

    public void setCouponDiscountPrice(Long couponDiscountPrice) {
        this.couponDiscountPrice = couponDiscountPrice;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Long getOrderItemVat() {
        return orderItemVat;
    }

    public void setOrderItemVat(Long orderItemVat) {
        this.orderItemVat = orderItemVat;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderCustomerType getOrderCustomerType() {
        return orderCustomerType;
    }

    public void setOrderCustomerType(OrderCustomerType orderCustomerType) {
        this.orderCustomerType = orderCustomerType;
    }

    public String getRefundAccountBankName() {
        return refundAccountBankName;
    }

    public void setRefundAccountBankName(String refundAccountBankName) {
        this.refundAccountBankName = refundAccountBankName;
    }

    public String getRefundAccountHolder() {
        return refundAccountHolder;
    }

    public void setRefundAccountHolder(String refundAccountHolder) {
        this.refundAccountHolder = refundAccountHolder;
    }

    public String getDeliveryMessage() {
        return deliveryMessage;
    }

    public void setDeliveryMessage(String deliveryMessage) {
        this.deliveryMessage = deliveryMessage;
    }

    public String getDeliverMessage() {
        return deliverMessage;
    }

    public void setDeliverMessage(String deliverMessage) {
        this.deliverMessage = deliverMessage;
    }

    public String getDeliveryPhoneNumber() {
        return deliveryPhoneNumber;
    }

    public void setDeliveryPhoneNumber(String deliveryPhoneNumber) {
        this.deliveryPhoneNumber = deliveryPhoneNumber;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerPersonalCustomsCode() {
        return customerPersonalCustomsCode;
    }

    public void setCustomerPersonalCustomsCode(String customerPersonalCustomsCode) {
        this.customerPersonalCustomsCode = customerPersonalCustomsCode;
    }
}
