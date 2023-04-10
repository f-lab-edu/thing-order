package org.example.domain;

import java.time.LocalDateTime;

public class Payment {
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
