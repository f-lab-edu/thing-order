package org.example.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

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
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "couponConstraint")
    private List<ProductCoupon> productCoupon = new ArrayList<>();

    @OneToMany(mappedBy = "couponConstraint")
    private List<ShopCoupon> shopCoupon = new ArrayList<>();

    public CouponConstraint(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
