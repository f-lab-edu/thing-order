package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class AdditionalDeliveryFeeArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String zipCode;

    private String streetAddress;

    @Enumerated(EnumType.STRING)
    private AreaType areaType;
}
