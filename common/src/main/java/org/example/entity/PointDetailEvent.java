package org.example.entity;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
public class PointDetailEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;

    private Long saveId;

    private boolean isAllUsed;

    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    private LocalDateTime expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_constraint_id")
    private PointConstraint pointConstraint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_event_id")
    private PointEvent pointEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public PointDetailEvent(long amount, boolean isAllUsed, PointStatus pointStatus, User user) {
        this.amount = amount;
        this.isAllUsed = isAllUsed;
        this.pointStatus = pointStatus;
        this.user = user;
    }
}
