package org.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.entity.Order;
import org.example.entity.PaymentMethod;
import org.example.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final CouponService couponService;
    private final PointService pointService;

    public Order createMemberOrder(PaymentMethod paymentMethod, User user,
            List<CreateOrderItemRequest> itemsToOrder, Long pointDiscountPrice) throws Exception {
        this.checkUserRefundABankAndHolderAndAccountWhenPaymentMethodVirtualAccount(user,
                paymentMethod);
        this.productService.checkProductExist(
                itemsToOrder.stream().map(CreateOrderItemRequest::getProductId)
                        .collect(Collectors.toList()));
        this.productService.checkProductStockCount(itemsToOrder);
        this.couponService.checkUserCouponStatus(user,
                itemsToOrder.stream()
                        .filter(item -> item != null && item.getUserCouponId() != null)
                        .map(CreateOrderItemRequest::getUserCouponId).collect(Collectors.toList())
        );
        this.pointService.checkUserPoint(user.getId(), pointDiscountPrice);

        LocalDateTime now = LocalDateTime.now();

        Order newOrder = new Order();

        return newOrder;
    }

    private void checkUserRefundABankAndHolderAndAccountWhenPaymentMethodVirtualAccount(User user,
            PaymentMethod paymentMethod) throws Exception {
        if (paymentMethod == PaymentMethod.VirtualAccount && (user.getBank() == null
                || user.getBankAccountHolder() == null || user.getBankAccount() == null)) {
            throw new Exception("Refund bank information is necessary");
        }
    }
}
