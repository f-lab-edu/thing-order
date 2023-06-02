package org.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;

    private boolean isUsed;

    private LocalDateTime couponUsageDate;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @OneToMany(mappedBy = "coupons")
    private List<OrderItem> orderItem = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_constraint_id")
    private CouponConstraint couponConstraint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Coupon(boolean isUsed, CouponStatus couponStatus, CouponConstraint couponConstraint, User user) {
        this.isUsed = isUsed;
        this.couponStatus = couponStatus;
        this.couponConstraint = couponConstraint;
        this.user = user;
    }
}
