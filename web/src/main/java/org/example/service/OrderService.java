package org.example.service;

import org.example.domain.DeliveryType;
import org.example.domain.DiscountType;
import org.example.domain.OptionsType;
import org.example.domain.Order;
import org.example.domain.OrderCustomerType;
import org.example.domain.OrderItem;
import org.example.domain.OrderItemOption;
import org.example.domain.OrderStatus;
import org.example.domain.PaymentMethod;
import org.example.domain.Product;
import org.example.domain.ProductClassification;
import org.example.domain.ShippingType;
import org.example.domain.Shop;
import org.example.domain.ShopClassification;
import org.example.domain.User;
import org.example.domain.UserDeliveryAddresses;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class OrderService {
    public Order createMemberOrder() {
        LocalDateTime now = LocalDateTime.now();

        Shop tempShop = new Shop();
        tempShop.setId(1L);
        tempShop.setName("testShop");
        tempShop.setDescription("this is test shop");
        tempShop.setLogoImg("logoImg");
        tempShop.setIsCrawled(false);
        tempShop.setBaseShippingFee(3000L);
        tempShop.setJejuShippingFee(null);
        tempShop.setIslandShippingFee(null);
        tempShop.setFreeShippingFee(null);
        tempShop.setShopClassification(ShopClassification.PARTNERSHIP);

        Product productToOrder = new Product();
        productToOrder.setId(1L);
        productToOrder.setCoverImg("coverImg");
        productToOrder.setName("testProduct");
        productToOrder.setDiscountType(DiscountType.PERCENTAGE);
        productToOrder.setDiscountRate(10L);
        productToOrder.setPrice(10000L);
        productToOrder.setDiscountPrice(9000L);
        productToOrder.setIsFreeShipping(true);
        productToOrder.setProductClassification(ProductClassification.PARTNERSHIP);
        productToOrder.setShippingType(ShippingType.DOMESTIC);
        productToOrder.setBaseShippingFee(3000L);
        productToOrder.setJejuShippingFee(2000L);
        productToOrder.setIslandShippingFee(2000L);
        productToOrder.setIsCrawled(false);
        productToOrder.setStockCount(10L);
        productToOrder.setOptionsType(OptionsType.ABSENCE);
        productToOrder.setProductOption(null);

        tempShop.setProducts(List.of(productToOrder));

        UserDeliveryAddresses userDeliveryAddresses = new UserDeliveryAddresses();
        userDeliveryAddresses.setId(1L);
        userDeliveryAddresses.setCreatedAt(now);
        userDeliveryAddresses.setUpdatedAt(now);
        userDeliveryAddresses.setZipCode("zipCode");
        userDeliveryAddresses.setStreetAddress("street");
        userDeliveryAddresses.setDetailAddress("detail");
        userDeliveryAddresses.setPhoneNumberForDelivery("01099999999");
        userDeliveryAddresses.setReceiver("user");

        User user = new User();
        user.setEmail("hwibaski@gmail.com");
        user.setName("hwibaski");
        user.setId(1L);
        user.setPhoneNumber("01099007278");
        user.setDeliveryAddresses(List.of(userDeliveryAddresses));

        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setId(1L);
        newOrderItem.setProduct(productToOrder);

        OrderItemOption orderItemOption = new OrderItemOption();
        orderItemOption.setOrderQuality(1L);

        newOrderItem.setOptions(orderItemOption);
        newOrderItem.setBaseShippingFee(productToOrder.getBaseShippingFee());
        newOrderItem.setJejuShippingFee(productToOrder.getJejuShippingFee());
        newOrderItem.setIslandShippingFee(productToOrder.getIslandShippingFee());
        newOrderItem.setOrderQuantity(1L);
        newOrderItem.setCancelReason(null);
        newOrderItem.setRefundReason(null);
        newOrderItem.setOrderItemTotalPaymentAmount(9000L);
        newOrderItem.setOrderItemTotalAmount(10000L);
        newOrderItem.setPointDiscountAmount(0L);
        newOrderItem.setCouponDiscountAmount(0L);
        newOrderItem.setProductDiscountAmount(1000L);
        newOrderItem.setCreatedAt(now);
        newOrderItem.setUpdatedAt(now);
        newOrderItem.setOrderStatusDate(now);
        newOrderItem.setOrderStatus(OrderStatus.PENDING);
        newOrderItem.setShop(tempShop);
        newOrderItem.setDeliveryFee(3000L);
        newOrderItem.setBaseShippingFee(3000L);
        newOrderItem.setIslandShippingFee(0L);
        newOrderItem.setJejuShippingFee(0L);
        newOrderItem.setOriginBaseShippingFee(3000L);
        newOrderItem.setConditionalFreeDeliveryFeeStandardByShop(0L);
        newOrderItem.setIsAcceptedConditionalFreeDeliveryFee(false);
        newOrderItem.setIsAcceptedConditionalFreeDeliveryFeeWhenOrder(false);
        newOrderItem.setDeliveryReleaseDate(null);
        newOrderItem.setReceiptCompletedDate(null);
        newOrderItem.setIslandShippingFee(0L);
        newOrderItem.setCreatedAt(now);
        newOrderItem.setUpdatedAt(now);
        newOrderItem.setShop(tempShop);

        Order newOrder = new Order();
        newOrder.setId(1L);
        newOrder.setCreatedAt(now);
        newOrder.setUpdatedAt(now);
        newOrder.setOrderNumber("2023417asvefa");
        newOrder.setOrderName("orderName");
        newOrder.setTotalOriginalPrice(10000L);
        newOrder.setTotalDiscountPrice(1000L);
        newOrder.setProductDiscountPrice(1000L);
        newOrder.setCouponDiscountPrice(0L);
        newOrder.setPointDiscountPrice(0L);
        newOrder.setDeliveryFee(3000L);
        newOrder.setPaymentMethod(PaymentMethod.Card);
        newOrder.setOrderCustomerType(OrderCustomerType.MemberOrder);
        newOrder.setRefundAccountHolder(null);
        newOrder.setRefundBankAccount(null);
        newOrder.setRefundAccountBankName(null);
        newOrder.setDeliveryMessage("deliveryMessage");
        newOrder.setDeliveryPhoneNumber(userDeliveryAddresses.getPhoneNumberForDelivery());
        newOrder.setReceiver(userDeliveryAddresses.getReceiver());
        newOrder.setStreetAddress(userDeliveryAddresses.getStreetAddress());
        newOrder.setDetailAddress(userDeliveryAddresses.getDetailAddress());
        newOrder.setZipCode(userDeliveryAddresses.getZipCode());
        newOrder.setPaymentDate(null);
        newOrder.setCustomerEmail(user.getEmail());
        newOrder.setCustomerName(user.getName());
        newOrder.setCustomerPhoneNumber(user.getPhoneNumber());
        newOrder.setCustomerPersonalCustomsCode(user.getPersonalCustomsCode());
        newOrder.setItems(List.of(newOrderItem));
        newOrder.setDeliveryType(DeliveryType.NORMAL);

        newOrder.setCustomer(user);
        return newOrder;
    }
}
