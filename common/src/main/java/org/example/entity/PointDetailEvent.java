package org.example.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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
}
