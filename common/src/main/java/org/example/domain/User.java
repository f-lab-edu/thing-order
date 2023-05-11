package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Getter
@Setter
public class User {
    @GeneratedValue @Id
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private List<UserDeliveryAddresses> deliveryAddresses;
    private String personalCustomsCode;
    private String bankAccountHolder;
    private String bankAccount;
}
