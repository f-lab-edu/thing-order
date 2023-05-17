package org.example.entity;

public enum CouponStatus {
    Available("Available"),
    Reserved("Reserved"),
    Used("Used"),
    Expired("Expired"),
    DeletedByAdmin("DeletedByAdmin");

    CouponStatus(String couponStatus) {
    }
}
