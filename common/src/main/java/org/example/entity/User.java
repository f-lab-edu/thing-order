package org.example.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String name;

    private String phoneNumber;

    private String personalCustomsCode;

    private String bankAccountHolder;

    private String bankAccount;

    @OneToMany(mappedBy = "customer")
    private List<Order> order = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserDeliveryAddress> deliveryAddresses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;
}
