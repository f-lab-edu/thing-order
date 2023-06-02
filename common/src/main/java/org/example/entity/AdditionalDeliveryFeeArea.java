package org.example.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
