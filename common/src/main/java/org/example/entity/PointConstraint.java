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

import org.example.config.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
@TypeDef(name = "psql_enum", typeClass = PostgreSQLEnumType.class)

public class PointConstraint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private boolean isAvailableWithCoupon;

    private boolean isActive;

    private long discount;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "point_constraint_discount_type_enum")
    @Type(type = "psql_enum")
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "point_constraint_point_target_enum")
    @Type(type = "psql_enum")
    private PointTarget pointTarget;

    private LocalDateTime validUntil;

    private Long totalIssuedPoint;

    private Long totalUsedPoint;

    @Column(nullable = true)
    private LocalDateTime issueReservationDate;

    @Column(nullable = true)
    private LocalDateTime startDateForSavePoint;

    @Column(nullable = true)
    private LocalDateTime endDateForSavePoint;

    @OneToMany(mappedBy = "pointConstraint")
    private List<PointEvent> pointEvent = new ArrayList<>();

    @OneToMany(mappedBy = "pointConstraint")
    private List<PointDetailEvent> pointDetailEvents = new ArrayList<>();

    public PointConstraint(String name, String description, boolean isAvailableWithCoupon, boolean isActive,
        long discount,
        DiscountType discountType, PointTarget pointTarget, Long totalIssuedPoint, Long totalUsedPoint) {
        this.name = name;
        this.description = description;
        this.isAvailableWithCoupon = isAvailableWithCoupon;
        this.isActive = isActive;
        this.discount = discount;
        this.discountType = discountType;
        this.pointTarget = pointTarget;
        this.totalIssuedPoint = totalIssuedPoint;
        this.totalUsedPoint = totalUsedPoint;
    }

    public void updateTotalUserPoint(long usedPointInThisOrder) {
        this.totalUsedPoint += usedPointInThisOrder;
    }
}
