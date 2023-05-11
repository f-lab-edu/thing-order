package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDeliveryAddresses {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String receiver;
    private String streetAddress;
    private String detailAddress;
    private String zipCode;
    private String phoneNumberForDelivery;
}
