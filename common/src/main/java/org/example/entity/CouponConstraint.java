package org.example.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.array.StringArrayType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@TypeDefs({
    @TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
    )
})
@Entity
@NoArgsConstructor
public class CouponConstraint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    @Type(type = "string-array")
    @Column(
        name = "guideline",
        columnDefinition = "text"
    )
    private String[] guideLine;

    private boolean isAvailableForMultipleUses;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private CouponCondition couponCondition;

    @Enumerated(EnumType.STRING)
    private SaleMethod saleMethod;

    private Long discount;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private LocalDateTime validUntil;

    private Long issueCount;

    private Long usedCount;

    private Long minPriceOfProduct;

    private Long maxPriceOfProduct;

    private LocalDateTime issueReservationDate;

    private LocalDateTime joinStartDate;

    private LocalDateTime joinEndDate;

    @Enumerated(EnumType.STRING)
    private CouponDiscountTarget discountTarget;

    @OneToMany(mappedBy = "couponConstraint")
    private final List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "couponConstraint")
    private final List<ProductCoupon> productCoupon = new ArrayList<>();

    @OneToMany(mappedBy = "couponConstraint")
    private final List<ShopCoupon> shopCoupon = new ArrayList<>();

    public CouponConstraint(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private boolean isValueType() {
        return this.discountType == DiscountType.Value;
    }

    private boolean isPercentType() {
        return this.discountType == DiscountType.Percentage;
    }

    public Long getTotalCouponDiscountPrice(Long orderItemTotalPaymentAmount, Long orderQuantity) {
        if (isValueType()) {
            return this.discount;
        } else if (isPercentType()) {
            return (long)Math.floor(
                (double)((orderItemTotalPaymentAmount / orderQuantity)
                    * this.discount) / 100);
        } else {
            return 0L;
        }
    }

    public void increaseTotalUsedCount() {
        this.usedCount++;
    }

    public CouponConstraint(String name, String description, boolean isActive, Long usedCount) {
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.usedCount = usedCount;
    }
}
