package org.example.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.example.config.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@TypeDef(name = "psql_enum", typeClass = PostgreSQLEnumType.class)
@Setter
@Getter
public class PointEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "point_event_point_status_enum")
    @Type(type = "psql_enum")
    private PointStatus pointStatus;

    private LocalDateTime expireDate;

    private Long amount;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "pointEvent")
    private List<PointDetailEvent> pointDetailEvent = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_constraint_id")
    private PointConstraint pointConstraint;

    public PointEvent(PointStatus pointStatus, Long amount, Order order, User user) {
        this.pointStatus = pointStatus;
        this.amount = amount;
        this.order = order;
        this.user = user;
    }

    public PointEvent(PointStatus pointStatus, Long amount, User user, PointConstraint pointConstraint) {
        this.pointStatus = pointStatus;
        this.amount = amount;
        this.pointConstraint = pointConstraint;
        this.user = user;
    }
}
