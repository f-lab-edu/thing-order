package org.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user_delivery_address")
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
}
