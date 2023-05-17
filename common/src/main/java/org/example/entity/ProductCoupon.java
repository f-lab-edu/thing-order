package org.example.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class ProductCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "coupon_constraint_id")
    private CouponConstraint couponConstraint;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
