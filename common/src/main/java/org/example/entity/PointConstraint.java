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
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    private PointTarget pointTarget;

    private LocalDateTime validUntil;

    private Long totalIssuedPoint;

    private Long totalUsedPoint;

    private LocalDateTime pointIssueReservationDate;

    private LocalDateTime startDateForSavePoint;

    private LocalDateTime endDateForSavePoint;

    @OneToMany(mappedBy = "pointConstraint")
    private List<PointEvent> pointEvent = new ArrayList<>();

    @OneToMany(mappedBy = "pointConstraint")
    private List<PointDetailEvent> pointDetailEvents = new ArrayList<>();
}
