package org.example.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
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

    @OneToMany(mappedBy = "user")
    private List<Cart> carts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public User(String email, String name, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(long id, String email, String name, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public User(String email, String name, String phoneNumber, String personalCustomsCode, String bankAccountHolder,
        String bankAccount, List<UserDeliveryAddress> deliveryAddresses, Bank bank) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.personalCustomsCode = personalCustomsCode;
        this.bankAccountHolder = bankAccountHolder;
        this.bankAccount = bankAccount;
        this.deliveryAddresses = deliveryAddresses;
        this.bank = bank;
    }

    public UserDeliveryAddress findAddressById(Long deliveryId) {
        return deliveryAddresses.stream()
            .filter(address -> address.getId() == deliveryId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(
                "User has no delivery address with the given ID"));
    }
}
