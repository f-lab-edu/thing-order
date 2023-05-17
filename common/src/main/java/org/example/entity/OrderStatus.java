package org.example.entity;

public enum OrderStatus {
    PENDING("Pending"),
    CANCEL("Cancel"),
    ORDER_FAILED("OrderFailed"),
    DEPOSIT_WAITING("DepositWaiting"),
    ORDER_COMPLETE("OrderComplete"),
    DELIVERY_PREPARATION("DeliveryPreparation"),
    DELIVERY_RELEASE("DeliveryRelease"),
    DELIVERY_COMPLETE("DeliveryComplete"),
    CALCULATE_COMPLETE("CalculateComplete"),
    REFUND_REQUEST("RefundRequest"),
    REFUND_COMPLETE("RefundComplete");

    OrderStatus(String orderStatus) {
    }
}
