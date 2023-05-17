package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
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
}
