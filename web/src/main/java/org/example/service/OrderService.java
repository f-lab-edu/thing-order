package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.entity.Order;
import org.example.entity.PaymentMethod;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    ProductRepository productRepository;

    public Order createMemberOrder(PaymentMethod paymentMethod, User user, List<CreateOrderItemRequest> itemsToOrder) throws Exception {
        this.checkUserRefundABankAndHolderAndAccountWhenPaymentMethodVirtualAccount(user, paymentMethod);
        this.checkProductExist(itemsToOrder);

        LocalDateTime now = LocalDateTime.now();

        Order newOrder = new Order();

        return newOrder;
    }

    private void checkUserRefundABankAndHolderAndAccountWhenPaymentMethodVirtualAccount(User user, PaymentMethod paymentMethod) throws Exception {
        if (paymentMethod == PaymentMethod.VirtualAccount && (user.getBank() == null || user.getBankAccountHolder() == null || user.getBankAccount() == null)) {
            throw new Exception("Refund bank information is necessary");
        }
    }

    private void checkProductExist(List<CreateOrderItemRequest> itemsToOrder) throws Exception {
        for (CreateOrderItemRequest createOrderItemRequest : itemsToOrder) {
            Optional<Product> product = productRepository.findById(createOrderItemRequest.getProductId());
            product.orElseThrow(() -> new GraphqlException("Could not find the product with ID"));
        }
    }
}
