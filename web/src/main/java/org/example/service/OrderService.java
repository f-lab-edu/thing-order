package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.entity.Order;
import org.example.entity.PaymentMethod;
import org.example.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    ProductService productService;

    public Order createMemberOrder(PaymentMethod paymentMethod, User user, List<CreateOrderItemRequest> itemsToOrder) throws Exception {
        this.checkUserRefundABankAndHolderAndAccountWhenPaymentMethodVirtualAccount(user, paymentMethod);
        this.productService.checkProductExist(itemsToOrder.stream().map(CreateOrderItemRequest::getProductId).collect(Collectors.toList()));

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
