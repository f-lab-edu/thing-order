package org.example.service;

import org.example.entity.Order;
import org.example.entity.PaymentMethod;
import org.example.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {
    public Order createMemberOrder(PaymentMethod paymentMethod, User user) throws Exception {
        this.checkUserRefundABankAndHolderAndAccountWhenPaymentMethodVirtualAccount(user, paymentMethod);
        LocalDateTime now = LocalDateTime.now();

        Order newOrder = new Order();

        return newOrder;
    }

    private void checkUserRefundABankAndHolderAndAccountWhenPaymentMethodVirtualAccount(User user, PaymentMethod paymentMethod) throws Exception {
        if (paymentMethod == PaymentMethod.VirtualAccount && (user.getBank() == null || user.getBankAccountHolder() == null || user.getBankAccount() == null)) {
            throw new Exception("Refund bank information is necessary");
        }
    }
}
