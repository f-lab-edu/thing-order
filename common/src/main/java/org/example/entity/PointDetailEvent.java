package org.example.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.example.config.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@TypeDef(name = "psql_enum", typeClass = PostgreSQLEnumType.class)
@Getter
@Setter
public class PointDetailEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long amount;

    private Long saveId;

    private Boolean isAllUsed;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "point_detail_event_point_status_enum")
    @Type(type = "psql_enum")
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

    public PointDetailEvent(long amount, Long saveId, PointStatus pointStatus, User user,
        PointConstraint pointConstraint, PointEvent pointEvent, Order order) {
        this.amount = amount;
        this.saveId = saveId;
        this.pointStatus = pointStatus;
        this.user = user;
        this.pointConstraint = pointConstraint;
        this.pointEvent = pointEvent;
        this.order = order;
    }

    public PointDetailEvent(long amount, Long saveId, PointStatus pointStatus, User user,
        PointConstraint pointConstraint, PointEvent pointEvent) {
        this.amount = amount;
        this.saveId = saveId;
        this.pointStatus = pointStatus;
        this.user = user;
        this.pointConstraint = pointConstraint;
        this.pointEvent = pointEvent;
    }

    public void setIsAllUsedToTrue() {
        this.isAllUsed = true;
    }
}
