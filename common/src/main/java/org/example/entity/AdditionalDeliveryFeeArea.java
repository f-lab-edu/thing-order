package org.example.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@RequiredArgsConstructor
public class AdditionalDeliveryFeeArea extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String zipCode;

    private String streetAddress;

    @Enumerated(EnumType.STRING)
    private AreaType areaType;

    @Builder
    public AdditionalDeliveryFeeArea(String zipCode, String streetAddress, AreaType areaType) {
        this.zipCode = zipCode;
        this.streetAddress = streetAddress;
        this.areaType = areaType;
    }
}
