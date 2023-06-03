package org.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

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
