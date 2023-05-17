package org.example.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PointEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    private LocalDateTime expireDate;

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

}
