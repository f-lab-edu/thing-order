package org.example.entity;

public enum OrderStatus {
    Pending("Pending"),
    Cancel("Cancel"),
    OrderFailed("OrderFailed"),
    DepositWaiting("DepositWaiting"),
    OrderComplete("OrderComplete"),
    DeliveryPreparation("DeliveryPreparation"),
    DeliveryRelease("DeliveryRelease"),
    DeliveryComplete("DeliveryComplete"),
    CalculateComplete("CalculateComplete"),
    RefundRequest("RefundRequest"),
    RefundComplete("RefundComplete");

    OrderStatus(String orderStatus) {
    }
}
