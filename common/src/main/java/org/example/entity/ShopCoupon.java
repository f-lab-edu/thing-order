package org.example.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class ShopCoupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "coupon_constraint_id")
    private CouponConstraint couponConstraint;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

}
