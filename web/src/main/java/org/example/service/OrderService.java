package org.example.service;

import org.example.dto.order.CheckAdditionalDeliveryFeeOutput;
import org.example.dto.order.CreateNewOrderItemResult;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.dto.order.NewOrderItemResult;
import org.example.dto.order.SortedOrderItem;
import org.example.entity.AreaType;
import org.example.entity.Coupon;
import org.example.entity.OptionsType;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import org.example.entity.PaymentMethod;
import org.example.entity.Product;
import org.example.entity.ProductOption;
import org.example.entity.User;
import org.example.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final CouponService couponService;
    private final PointService pointService;
    private final AdditionalDeliveryService additionalDeliveryService;
    private final OrderItemRepository orderItemRepository;

    public Order createMemberOrder(PaymentMethod paymentMethod, User user,
                                   List<CreateOrderItemRequest> itemsToOrder, Long pointDiscountPrice, Long deliveryId)
            throws Exception {
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

        NewOrderItemResult newOrderItemResult = this.newOrderItemResult(itemsToOrder,
                user.findAddressById(deliveryId).getZipCode(), user.getId());

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

    private NewOrderItemResult newOrderItemResult(List<CreateOrderItemRequest> itemsToOrder,
                                                  String zipCode, Long userId) {
        List<OrderItem> result = new ArrayList<>();
        Long totalProductDiscountPrice = 0L;
        Long totalDeliveryFee = 0L;
        boolean isAddressToChargeAdditionalFee = false;
        AreaType areaType = null;

        CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput = this.additionalDeliveryService.checkAdditionalDeliveryFee(
                zipCode);

        if (checkAdditionalDeliveryFeeOutput.isOk()
                && checkAdditionalDeliveryFeeOutput.isAddressToChargeAdditionalFee()
        ) {
            areaType = checkAdditionalDeliveryFeeOutput.getAreaType();
            isAddressToChargeAdditionalFee = true;
        }

        for (CreateOrderItemRequest item : itemsToOrder) {
            Product product = this.productService.findProductByDisplayedAndShopDisplayed(
                    item.getProductId());

            CreateNewOrderItemResult createNewOrderItemResult = this.createNewOrderItemObjectV2(
                    product, item.getOptionId(), item.getOrderQuantity(),
                    isAddressToChargeAdditionalFee, areaType);

            if (item.getUserCouponId() != null) {
                Coupon coupon = this.couponService.findUserCoupon(userId, item.getUserCouponId());

                createNewOrderItemResult.getOrderItem().setCoupons(coupon);
            }

            totalProductDiscountPrice += createNewOrderItemResult.getProductDiscountPrice();
            totalDeliveryFee += createNewOrderItemResult.getDeliveryFee();

            result.add(createNewOrderItemResult.getOrderItem());
        }

        Map<Long, SortedOrderItem> sortedOrderItemsByShopId = new HashMap<>();

        for (OrderItem orderItem : result) {
            if (!sortedOrderItemsByShopId.containsKey(orderItem.getShop().getId())) {
                sortedOrderItemsByShopId.put(orderItem.getShop().getId(),
                        new SortedOrderItem(orderItem.getShop()));
            }

            sortedOrderItemsByShopId.get(orderItem.getShop().getId()).getOrderItems()
                    .add(orderItem);
        }

        for (Map.Entry<Long, SortedOrderItem> entry : sortedOrderItemsByShopId.entrySet()) {
            SortedOrderItem sortedOrderItem = entry.getValue();

            OrderItem maxOriginBaseShippingFeeItem = sortedOrderItem.getOrderItems()
                    .stream()
                    .reduce((prev, cur) ->
                            prev.getOriginBaseShippingFee() >= cur.getOriginBaseShippingFee()
                                    ? prev : cur)
                    .orElse(null);

            OrderItem maxOriginJejuShippingFeeItem = sortedOrderItem.getOrderItems().stream()
                    .reduce((prev, cur) ->
                            prev.getOriginJejuShippingFee() >= cur.getOriginJejuShippingFee()
                                    ? prev : cur)
                    .orElse(null);

            OrderItem maxOriginIsalndShippingFeeItem = sortedOrderItem.getOrderItems().stream()
                    .reduce((prev, cur) ->
                            prev.getOriginIslandShippingFee() >= cur.getOriginIslandShippingFee()
                                    ? prev
                                    : cur)
                    .orElse(null);

            sortedOrderItem.setMaxOriginBaseShippingFeeItem(maxOriginBaseShippingFeeItem);
            sortedOrderItem.setMaxOriginJejuShippingFeeItem(maxOriginJejuShippingFeeItem);
            sortedOrderItem.setMaxOriginIslandShippingFeeItem(maxOriginIsalndShippingFeeItem);
        }

        for (Map.Entry<Long, SortedOrderItem> entry : sortedOrderItemsByShopId.entrySet()) {
            SortedOrderItem sortedOrderItem = entry.getValue();

            boolean isMaxBaseShippingItemChecked = false;
            for (OrderItem orderItem : sortedOrderItem.getOrderItems()) {
                if (orderItem.getProduct().getId()
                        == sortedOrderItem.getMaxOriginBaseShippingFeeItem()
                        .getProduct().getId()
                        && !isMaxBaseShippingItemChecked) {
                    orderItem.setBaseShippingFee(
                            sortedOrderItem.getMaxOriginBaseShippingFeeItem().getBaseShippingFee());
                    isMaxBaseShippingItemChecked = true;
                } else {
                    orderItem.setBaseShippingFee(0L);
                }
            }

            boolean isMaxJejuShippingItemChecked = false;
            for (OrderItem orderItem : sortedOrderItem.getOrderItems()) {
                if (orderItem.getProduct().getId()
                        == sortedOrderItem.getMaxOriginJejuShippingFeeItem()
                        .getProduct().getId()
                        && !isMaxJejuShippingItemChecked) {
                    orderItem.setJejuShippingFee(
                            sortedOrderItem.getMaxOriginJejuShippingFeeItem().getJejuShippingFee());
                    isMaxJejuShippingItemChecked = true;
                } else {
                    orderItem.setJejuShippingFee(0L);
                }
            }

            boolean isMaxIslandShippingItemChecked = false;
            for (OrderItem orderItem : sortedOrderItem.getOrderItems()) {
                if (orderItem.getProduct().getId()
                        == sortedOrderItem.getMaxOriginIslandShippingFeeItem()
                        .getProduct().getId()
                        && !isMaxIslandShippingItemChecked) {
                    orderItem.setIslandShippingFee(
                            sortedOrderItem.getMaxOriginIslandShippingFeeItem()
                                    .getIslandShippingFee());
                    isMaxIslandShippingItemChecked = true;
                } else {
                    orderItem.setIslandShippingFee(0L);
                }
            }
        }

        for (Map.Entry<Long, SortedOrderItem> entry : sortedOrderItemsByShopId.entrySet()) {
            SortedOrderItem sortedOrderItem = entry.getValue();

            long totalPaymentAmountByShop = sortedOrderItem.getOrderItems().stream()
                    .mapToLong(OrderItem::getOrderItemTotalPaymentAmount)
                    .sum();

            boolean isFulfillFreeDeliveryCondition =
                    sortedOrderItem.getShop().getFreeShippingFee() != null
                            && totalPaymentAmountByShop >= sortedOrderItem.getShop()
                            .getFreeShippingFee();

            if (isFulfillFreeDeliveryCondition) {
                for (OrderItem orderItem : sortedOrderItem.getOrderItems()) {
                    orderItem.setBaseShippingFee(0L);
                    orderItem.setAcceptedConditionalFreeDeliveryFee(true);
                    orderItem.setAcceptedConditionalFreeDeliveryFeeWhenOrder(true);
                }
            }
        }

        for (Map.Entry<Long, SortedOrderItem> entry : sortedOrderItemsByShopId.entrySet()) {
            SortedOrderItem sortedOrderItem = entry.getValue();

            sortedOrderItem.getOrderItems().forEach(orderItem -> {
                orderItem.setDeliveryFee(
                        orderItem.getBaseShippingFee() + orderItem.getJejuShippingFee()
                                + orderItem.getIslandShippingFee());
            });
        }

        List<OrderItem> orderItemList = new ArrayList<>();

        for (Map.Entry<Long, SortedOrderItem> entry : sortedOrderItemsByShopId.entrySet()) {
            SortedOrderItem sortedOrderItem = entry.getValue();

            orderItemList.addAll(sortedOrderItem.getOrderItems());
        }

        this.orderItemRepository.saveAll(orderItemList);

        return new NewOrderItemResult(orderItemList, totalProductDiscountPrice, totalDeliveryFee);
    }


    private CreateNewOrderItemResult createNewOrderItemObjectV2(Product product, long optionId,
                                                                long orderQuantity,
                                                                boolean isAddressToChargeAdditionalFee, AreaType areaType) {
        long orderItemTotalAmount = 0;
        long orderItemTotalPaymentAmount = 0;
        long productDiscountPrice = 0;
        long deliveryFee = 0;
        long jejuShippingFee = 0;
        long islandShippingFee = 0;
        ProductOption userSelectOptions = null;

        if (product.getOptions() != null && product.getOptionsType() != OptionsType.Absence) {
            userSelectOptions =
                    product.getOptions().stream().filter(option -> option.getOptionId() == optionId)
                            .findFirst().get();
        }

        boolean noOptionPriceProduct = product.getOptionsType() == OptionsType.Absence;
        boolean noOptionPriceAndNoDiscountProduct =
                noOptionPriceProduct && product.getDiscountType() == null;
        boolean noOptionPriceAndDiscountProduct =
                noOptionPriceProduct && product.getDiscountType() != null;
        boolean hasOptionPriceProduct = product.getOptionsType() == OptionsType.Combination
                || product.getOptionsType() == OptionsType.Solo;
        boolean hasOptionPriceAndNoDiscountProduct =
                hasOptionPriceProduct && product.getDiscountType() == null;
        boolean hasOptionPriceAndDiscountProduct =
                hasOptionPriceProduct && product.getDiscountType() != null;

        // 주문 아이템에 옵션 가격이 없을 경우 (단독형, 옵션 없음)
        if (noOptionPriceProduct) {
            orderItemTotalAmount += product.getPrice() * orderQuantity;
        }

        if (noOptionPriceAndNoDiscountProduct) {
            orderItemTotalPaymentAmount += product.getPrice() * orderQuantity;
        }

        if (noOptionPriceAndDiscountProduct) {
            orderItemTotalPaymentAmount += product.getDiscountPrice() * orderQuantity;
            productDiscountPrice +=
                    (product.getPrice() - product.getDiscountPrice()) * orderQuantity;
        }

        if (hasOptionPriceProduct) {
            assert userSelectOptions != null;
            orderItemTotalAmount +=
                    (product.getPrice() + userSelectOptions.getOptionPrice()) * orderQuantity;
        }

        if (hasOptionPriceAndNoDiscountProduct) {
            orderItemTotalPaymentAmount +=
                    (product.getPrice() + userSelectOptions.getOptionPrice()) * orderQuantity;
        }

        if (hasOptionPriceAndDiscountProduct) {
            orderItemTotalPaymentAmount +=
                    (product.getDiscountPrice() + userSelectOptions.getOptionPrice())
                            * orderQuantity;

            productDiscountPrice +=
                    (product.getPrice() - product.getDiscountPrice()) * orderQuantity;
        }

        if (isAddressToChargeAdditionalFee && areaType == AreaType.Jeju) {
            jejuShippingFee += product.getJejuShippingFee();
        }

        if (isAddressToChargeAdditionalFee && areaType == AreaType.AreaExceptForJeju) {
            islandShippingFee += product.getIslandShippingFee();
        }

        Long conditionalFreeDeliveryFeeStandardByShop =
                product.getShop().getFreeShippingFee() != null ? product.getShop()
                        .getFreeShippingFee() : 0;

        OrderItem newOrderItem = new OrderItem(product, orderItemTotalPaymentAmount, orderQuantity,
                orderItemTotalPaymentAmount, productDiscountPrice, LocalDateTime.now(),
                OrderStatus.PENDING, deliveryFee, product.getBaseShippingFee(), jejuShippingFee,
                islandShippingFee, jejuShippingFee, islandShippingFee, product.getBaseShippingFee(),
                conditionalFreeDeliveryFeeStandardByShop, false, deliveryFee);

        return new CreateNewOrderItemResult(newOrderItem, productDiscountPrice, deliveryFee);
    }
}
