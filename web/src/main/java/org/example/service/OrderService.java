package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CreateOrderItemRequest;
import org.example.entity.*;
import org.example.message.*;
import org.example.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final AdditionalDeliveryService additionalDeliveryService;
    private final CartService cartService;
    private final CouponService couponService;
    private final OrderRepository orderRepository;
    private final PointService pointService;
    private final ProductService productService;

    public Order createMemberOrder(PaymentMethod paymentMethod, User user,
                                   List<CreateOrderItemRequest> itemsToOrder, Long pointDiscountPrice, Long deliveryId, String deliveryMessage)
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

        Order order = this.newOrderObject(user, newOrderItemResult.getOrderItems(),
                newOrderItemResult.getCheckAdditionalDeliveryFeeOutput(), paymentMethod, deliveryMessage, deliveryId);
        order.getItems().forEach(orderItem -> orderItem.setOrder(order));

        Order couponAppliedOrder = this.calculateCouponUsageInOrder(order);
        Order pointAppliedOrder = this.calculatePointUsageInOrder(couponAppliedOrder, pointDiscountPrice);

        Order savedOrder = this.orderRepository.save(pointAppliedOrder);

        if (savedOrder.isZeroPaid()) {
            return this.zeroPaidProcess(savedOrder);
        }

        return savedOrder;
    }

    private Order zeroPaidProcess(Order order) {
        order.setPaymentDate(LocalDateTime.now());

        for (OrderItem item : order.getItems()) {
            item.setOrderStatusToComplete();
        }

        List<ProductStockCountMessage> productStockCountMessages = order.getItems()
                .stream()
                .map(orderItem -> new ProductStockCountMessage(orderItem.getProduct(), orderItem.getOptionId(),
                        orderItem.getOrderQuantity()))
                .collect(
                        Collectors.toList());

        this.productService.decreaseProductStockCount(productStockCountMessages);

        if (order.hasPointDiscountPrice()) {
            this.pointService.usePoint(order.getCustomer(), order.getPointDiscountPrice(), order);
        }

        if (order.hasCouponDiscountPrice()) {
            List<Coupon> usedCoupons = order.getItems().stream()
                    .filter(OrderItem::hasCouponProp)
                    .map(OrderItem::getCoupons)
                    .collect(Collectors.toList());

            this.couponService.useCoupon(usedCoupons);
        }

        List<DeleteCartItemMessage> deleteCartItemMessages = order.getItems()
                .stream()
                .map(orderItem -> new DeleteCartItemMessage(orderItem.getProduct().getId(), orderItem.getProduct()
                        .getOptionsType(), orderItem.getOptionId()))
                .collect(Collectors.toList());

        this.cartService.deleteCartItems(order.getCustomer().getId(), deleteCartItemMessages);

        return order;
    }

    private Order calculatePointUsageInOrder(Order order, Long pointDiscountAmount) {
        Long point = pointDiscountAmount;

        if (point <= 0) {
            return order;
        }

        List<OrderItem> orderItems = this.sortOrderItemsByHighPrice(order.getItems());

        for (OrderItem orderItem : orderItems) {
            if (point == 0) {
                break;
            }

            if (orderItem.getOrderItemTotalPaymentAmount() - point > 0) {
                orderItem.applyPointDiscountAmount(point);

                point = 0L;
            } else if (orderItem.getOrderItemTotalPaymentAmount() - point <= 0) {
                point -= orderItem.getOrderItemTotalPaymentAmount();
                orderItem.applyPointDiscountAmount(orderItem.getOrderItemTotalPaymentAmount());
            }
        }

        return order;
    }

    private List<OrderItem> sortOrderItemsByHighPrice(List<OrderItem> orderItems) {
        orderItems.sort(Comparator.comparingLong((OrderItem item) ->
                        item.getOrderItemTotalAmount() - item.getProductDiscountAmount() - item.getCouponDiscountAmount())
                .reversed());

        return orderItems;
    }

    private Order calculateCouponUsageInOrder(Order order) {
        for (OrderItem orderItem : order.getItems()) {
            if (orderItem.hasNotCouponToUse()) {
                continue;
            }

            CouponConstraint couponConstraint = orderItem.getCoupons().getCouponConstraint();
            Long couponDiscountPrice = couponConstraint.getTotalCouponDiscountPrice(orderItem.getOrderItemTotalAmount(),
                    orderItem.getOrderQuantity());

            orderItem.applyCouponDiscountAmount(couponDiscountPrice);
        }

        return order;
    }

    private Order newOrderObject(User user, List<OrderItem> orderItems,
                                 CheckAdditionalDeliveryFeeOutput checkAdditionalDeliveryFeeOutput, PaymentMethod paymentMethod,
                                 String deliveryMessage, Long deliveryId) {
        List<Product> products = orderItems.stream().map(OrderItem::getProduct)
                .collect(Collectors.toList());

        UserDeliveryAddress userDeliveryAddress = user.findAddressById(deliveryId);

        String orderName = this.createOrderName(products, products.size());
        String orderNumber = this.createOrderNumber();

        return new Order(orderName, orderNumber, paymentMethod, OrderCustomerType.MemberOrder,
                deliveryMessage, userDeliveryAddress.getPhoneNumberForDelivery(), userDeliveryAddress.getReceiver(),
                userDeliveryAddress.getStreetAddress(), userDeliveryAddress.getDetailAddress(),
                userDeliveryAddress.getZipCode(), user.getEmail(), user.getName(), user.getPhoneNumber(),
                user.getPersonalCustomsCode(), orderItems, user,
                checkAdditionalDeliveryFeeOutput.isAddressToChargeAdditionalFee(),
                checkAdditionalDeliveryFeeOutput.getAreaType());
    }

    private String createOrderNumber() {
        LocalDateTime now = LocalDateTime.now();

        return String.format("%04d%01d%02d%s",
                now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                Integer.toString(now.getNano(), 36));
    }

    private String createOrderName(List<Product> products, int totalCountOfOrderedProduct) {
        if (totalCountOfOrderedProduct == 1) {
            return products.get(0).getName();
        } else {
            return products.get(0).getName() + " 외 " + (totalCountOfOrderedProduct - 1) + " 건";
        }
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

        return new NewOrderItemResult(orderItemList, checkAdditionalDeliveryFeeOutput);
    }

    private CreateNewOrderItemResult createNewOrderItemObjectV2(Product product, Long optionId,
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
                orderItemTotalAmount, productDiscountPrice, LocalDateTime.now(),
                OrderStatus.Pending, deliveryFee, product.getBaseShippingFee(), jejuShippingFee,
                islandShippingFee, jejuShippingFee, islandShippingFee, product.getBaseShippingFee(),
                conditionalFreeDeliveryFeeStandardByShop, false, deliveryFee, product.getShop(),
                userSelectOptions);

        return new CreateNewOrderItemResult(newOrderItem, productDiscountPrice, deliveryFee);
    }
}
