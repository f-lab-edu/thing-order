package org.example.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String paymentKey;

    private String orderNumber;

    private Long totalAmount;

    private String method;

    private String bank;

    private String accountNumber;

    private Long vat;

    private String accountHolder;

    private LocalDateTime accountExpireDate;

    private String cardCompany;

    private String cardNumber;

    private Long installmentPlanMonths;

    private String paymentStatus;
}
