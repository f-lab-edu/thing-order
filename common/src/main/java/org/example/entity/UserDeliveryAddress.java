package org.example.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_delivery_address")
@NoArgsConstructor
public class UserDeliveryAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String receiver;

    private String streetAddress;

    private String detailAddress;

    private String zipCode;

    private String phoneNumberForDelivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserDeliveryAddress(long id, String receiver, String streetAddress, String detailAddress, String zipCode,
        String phoneNumberForDelivery) {
        this.id = id;
        this.receiver = receiver;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
        this.phoneNumberForDelivery = phoneNumberForDelivery;
    }
}
