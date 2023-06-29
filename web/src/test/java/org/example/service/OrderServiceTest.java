package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.dto.order.CheckAdditionalDeliveryFeeOutput;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.entity.AreaType;
import org.example.entity.Bank;
import org.example.entity.DeliveryType;
import org.example.entity.DiscountType;
import org.example.entity.OptionsType;
import org.example.entity.Order;
import org.example.entity.OrderCustomerType;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import org.example.entity.PaymentMethod;
import org.example.entity.Product;
import org.example.entity.ProductClassification;
import org.example.entity.ProductOption;
import org.example.entity.ShippingType;
import org.example.entity.Shop;
import org.example.entity.User;
import org.example.entity.UserDeliveryAddress;
import org.example.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CouponService couponService;

    @Mock
    private PointService pointService;

    @Mock
    private AdditionalDeliveryService additionalDeliveryService;

    @Mock
    private User mockedUser;
    private List<CreateOrderItemRequest> tempCreateOrderItemRequests;
    private PaymentMethod tempPaymentMethod;
    private Long tempDeliveryId;

    private Product optionTestProduct;

    private Product noOptionTestProduct;

    private String tempDeliveryMessage;

    @BeforeEach
    void beforeEach() {
        Bank bank = new Bank("우리", "20");

        UserDeliveryAddress userDeliveryAddress = new UserDeliveryAddress(1L, "testReceiver", "street address",
            "detail address", "zip code", "01099999999");

        mockedUser = new User("test@gmail.com", "testUser", "01012341234", "", "testUser", "123456789",
            List.of(userDeliveryAddress), bank);
        mockedUser.setId(1L);

        Shop shop = new Shop("testShop", true);

        ProductOption productOption1 = new ProductOption(1L, "종류", "랜덤1마리", "", "", "", "", 144L, 0L);

        ProductOption productOption2 = new ProductOption(2L, "색상", "파랑", "", "", "", "", 300L,
            0L);

        optionTestProduct = new Product("test product 1", DiscountType.Value, 2810L, 1690L, 4500L, false,
            ProductClassification.Partnership, ShippingType.Domestic, 3000L, 3000L, 3000L, true, 300L,
            OptionsType.Combination, shop, List.of(productOption1, productOption2));

        noOptionTestProduct = new Product("test product 2", DiscountType.Value, 1000L, 3500L, 4500L, false,
            ProductClassification.Partnership, ShippingType.Domestic, 5000L, 3000L, 3000L, true, 300L,
            OptionsType.Absence, shop, null);

        CreateOrderItemRequest tempCreateOrderItemRequest = new CreateOrderItemRequest();
        tempCreateOrderItemRequest.setProductId(1L);
        tempCreateOrderItemRequest.setOptionId(1L);
        tempCreateOrderItemRequest.setOrderQuantity(1L);
        tempCreateOrderItemRequests = List.of(tempCreateOrderItemRequest);

        tempDeliveryId = 1L;
        tempPaymentMethod = PaymentMethod.Card;
        tempDeliveryMessage = "temp delivery message";
    }

    @Test
    @DisplayName("orderService 유효성 검사")
    void orderServiceDi() {
        Assertions.assertNotNull(orderService);
    }

    @Nested
    @DisplayName("createMemberOrder() 메서드 테스트")
    class CreateMemberOrderTest {

        @Test
        @DisplayName("Order의 totalOriginalPrice는 OrderItem들의 orderItemTotalAmount의 합이다.")
        void createOrderTest1() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            Long totalOriginalPrice = order.getTotalOriginalPrice();
            Long sumOfOrderItemTotalAmount = order.getItems().stream()
                .mapToLong(OrderItem::getOrderItemTotalAmount).sum();

            assertThat(totalOriginalPrice).isEqualTo(sumOfOrderItemTotalAmount);
        }

        @Test
        @DisplayName("Order의 totalDiscountPrice는 나머지 할인 프로퍼티의 합이다")
        void createOrderTest2() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            Long totalDiscountPrice = order.getTotalDiscountPrice();
            Long sumOfDiscountPrice =
                order.getProductDiscountPrice() + order.getCouponDiscountPrice()
                    + order.getPointDiscountPrice();

            assertThat(totalDiscountPrice).isEqualTo(sumOfDiscountPrice);
        }

        @Test
        @DisplayName("Order의 deliveryFee는 orderItem의 deliveryFee의 합이다.")
        void createOrderTest3() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);
            Long orderDeliveryFee = order.getDeliveryFee();
            Long sumOfDeliveryFee = order.getItems().stream().mapToLong(OrderItem::getDeliveryFee)
                .sum();

            assertThat(orderDeliveryFee).isEqualTo(sumOfDeliveryFee);
        }

        @Test
        @DisplayName("Order의 배송지 관련 정보 null check")
        void createOrderTest4() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertAll(
                () -> assertThat(order.getStreetAddress()).isNotNull(),
                () -> assertThat(order.getDetailAddress()).isNotNull(),
                () -> assertThat(order.getZipCode()).isNotNull(),
                () -> assertThat(order.getReceiver()).isNotNull(),
                () -> assertThat(order.getDeliveryType()).isNotNull(),
                () -> assertThat(order.getDeliveryMessage()).isNotNull(),
                () -> assertThat(order.getDeliveryPhoneNumber()).isNotNull()
            );
        }

        @Test
        @DisplayName("Order의 주문 고객 정보 null check")
        void createOrderTest5() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertAll(
                () -> assertThat(order.getCustomerEmail()).isNotNull(),
                () -> assertThat(order.getCustomer()).isNotNull(),
                () -> assertThat(order.getOrderCustomerType()).isNotNull()
            );
        }

        @Test
        @DisplayName("Order의 주문번호, 결제 방식 null check")
        void createOrderTest6() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertAll(
                () -> assertThat(order.getOrderNumber()).isNotNull(),
                () -> assertThat(order.getPaymentMethod()).isNotNull()
            );
        }

        @Test
        @DisplayName("orderItemTotalPaymentAmount는 orderItemTotalAmount - (productDiscountAmount + couponDiscountAmount + productDiscountAmount) 이다.")
        void createOrderTest7() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            order.getItems()
                .forEach(orderItem -> assertThat(orderItem.getOrderItemTotalPaymentAmount())
                    .isEqualTo(orderItem.getOrderItemTotalAmount() - (
                        orderItem.getProductDiscountAmount()
                            + orderItem.getPointDiscountAmount()
                            + orderItem.getCouponDiscountAmount())
                    ));
        }

        @Test
        @DisplayName("orderItem의 상태는 PENDING이어야 한다.")
        void createOrderTest8() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            order.getItems().forEach(orderItem -> assertThat(orderItem.getOrderStatus()).isEqualTo(
                OrderStatus.Pending));
        }

        @Test
        @DisplayName("Order의 deliveryType이 Normal이면 각 orderItem의 deliveryFee는 baseShippingFee와 같은 값이다")
        void createOrderTest9() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getDeliveryType()).isEqualTo(DeliveryType.NORMAL);
            order.getItems()
                .forEach(orderItem -> assertThat(orderItem.getDeliveryFee()).isEqualTo(
                    orderItem.getBaseShippingFee()));
        }

        @Test
        @DisplayName("Order의 deliveryType이 Jeju면 각 orderItem의 deliveryFee는 baseShippingFee + jejuShippingFee와 같은 값이다")
        void createOrderTest10() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, true, AreaType.Jeju));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getDeliveryType()).isEqualTo(DeliveryType.JEJU);
            order.getItems()
                .forEach(orderItem -> {
                    assertThat(orderItem.getDeliveryFee()).isEqualTo(
                        orderItem.getJejuShippingFee() + orderItem.getBaseShippingFee());
                });
        }

        @Test
        @DisplayName("Order의 deliveryType이 Island면 각 orderItem의 deliveryFee는 baseShippingFee + islandShippingFee와 같은 값이다")
        void createOrderTest11() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, true, AreaType.AreaExceptForJeju));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getDeliveryType()).isEqualTo(DeliveryType.ISLAND);
            order.getItems()
                .forEach(orderItem -> assertThat(orderItem.getDeliveryFee()).isEqualTo(
                    orderItem.getIslandShippingFee() + orderItem.getBaseShippingFee()));
        }

        @Test
        @DisplayName("Order 객체에서 paymentDate는 null이어야 한다")
        void createdOrderTest12() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertAll(
                () -> assertThat(order.getPaymentDate()).isNull()
            );
        }

        @Test
        @DisplayName("Order의 items 리스트는 널이 아니여야 한다")
        void createOrderTest13() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getItems()).isNotNull();
        }

        @Test
        @DisplayName("Order의 customer 필드는 null이 아니여야 한다")
        void createOrderTest14() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getCustomer()).isNotNull();
        }

        @Test
        @DisplayName("Order의 OrderItem의 product 필드는 null이 아니여야 한다")
        void createOrderTest15() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            order.getItems().forEach(orderItem -> assertThat(orderItem.getProduct()).isNotNull());
        }

        @Test
        @DisplayName("Order의 OrderItem의 shop 필드는 null이 아니여야 한다")
        void createOrderTest16() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            order.getItems().forEach(orderItem -> assertThat(orderItem.getShop()).isNotNull());
        }

        @Test
        @DisplayName("Order의 customerType은 MemberOrder여야 한다.")
        void createOrderTest17() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getOrderCustomerType()).isEqualTo(OrderCustomerType.MemberOrder);
        }

        @Test
        @DisplayName("Order의 PaymentMethod가 Card이면 환불 관련 정보는 null이어야한다.")
        void createOrderTest18() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getRefundBankAccount()).isNull();
            assertThat(order.getRefundAccountHolder()).isNull();
            assertThat(order.getRefundAccountBankName()).isNull();
        }

        @Test
        @DisplayName("Order의 PaymentMethod가 VirtualAccount이면 환불 관련 정보는 null이면 안된다..")
        void createOrderTest19() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getRefundBankAccount()).isNotNull();
            assertThat(order.getRefundAccountHolder()).isNotNull();
            assertThat(order.getRefundAccountBankName()).isNotNull();
        }

        @Test
        @DisplayName("Order의 totalDiscountPrice(총 할인금액)는 totalOriginalPrice(배송비를 제외한 금액)보다 작거나 같아야 한다.")
        void createOrderTest20() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            Order order = orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            assertThat(order.getTotalDiscountPrice()).isLessThanOrEqualTo(
                order.getTotalDiscountPrice());
        }

        @Test
        @DisplayName("주문 시 상품의 재고를 체크해야한다")
        void createOrderTest23() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);

            // when
            orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            verify(productService, times(1)).checkProductExist(List.of(productIdToOrder));
            verify(productService, times(1)).checkProductStockCount(List.of(createOrderItemRequest));

        }

        @Test
        @DisplayName("주문 시 쿠폰의 유효성 검사를 테스트 해야한다")
        void createOrderTest27() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, 1L,
                productOptionId, productOrderQuantity);

            // when
            orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            verify(couponService, times(1)).checkUserCouponStatus(mockedUser, Stream.of(createOrderItemRequest)
                .filter(item -> item != null && item.getUserCouponId() != null)
                .map(CreateOrderItemRequest::getUserCouponId).collect(Collectors.toList()));
        }

        @Test
        @DisplayName("주문에서 포인트를 사용하려면 해당 user의 남은 포인트가 충분해야 한다.")
        void createOrderTest28() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, 1L,
                productOptionId, productOrderQuantity);

            // when
            orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest), pointDiscountPrice, tempDeliveryId, tempDeliveryMessage);

            // then
            verify(pointService, times(1)).checkUserPoint(mockedUser.getId(), pointDiscountPrice);
        }

        @Test
        @DisplayName("같은 상점의 상품을 여러개 구매한 경우 하나의 상품에만 배송비(가장 비싼 배송비)가 적용되어야 한다.")
        void createOrderTest31() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            Long productIdToOrder2 = 2L;
            Long productOrderQuantity2 = 1L;

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder2)).willReturn(
                noOptionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);
            CreateOrderItemRequest createOrderItemRequest2 = new CreateOrderItemRequest(productIdToOrder2, null,
                null, productOrderQuantity2);

            // when
            Order order = orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest, createOrderItemRequest2), pointDiscountPrice, tempDeliveryId,
                tempDeliveryMessage);

            // then
            assertThat(order.getDeliveryFee()).isEqualTo(Stream.of(optionTestProduct, noOptionTestProduct)
                .map(Product::getBaseShippingFee)
                .max(Comparator.naturalOrder()).orElse(0L));
        }

        @Test
        @DisplayName("상점에서 조건부 무료 배송 기준을 지정했을 경우, 기준 금액 이상만큼의 상품을 구매했을 시 무료 배송이 적용되어야 한다.")
        void createOrderTest32() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 0L;

            Long productIdToOrder2 = 2L;
            Long productOrderQuantity2 = 1L;

            optionTestProduct.getShop().setFreeShippingFee(500L);

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder2)).willReturn(
                noOptionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);
            CreateOrderItemRequest createOrderItemRequest2 = new CreateOrderItemRequest(productIdToOrder2, null,
                null, productOrderQuantity2);

            // when
            Order order = orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest, createOrderItemRequest2), pointDiscountPrice, tempDeliveryId,
                tempDeliveryMessage);

            // then
            assertThat(order.getDeliveryFee()).isEqualTo(0L);
        }

        @Test
        @DisplayName("고객이 실제로 결제할 금액이 없을 때는 zeroPaidProcess 함수가 실행되어야 한다.")
        void createOrderTest33() throws Exception {
            // given
            Long productIdToOrder = 1L;
            Long productOptionId = 1L;
            Long productOrderQuantity = 1L;
            Long pointDiscountPrice = 100000L;

            Long productIdToOrder2 = 2L;
            Long productOrderQuantity2 = 1L;

            optionTestProduct.getShop().setFreeShippingFee(500L);

            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder)).willReturn(
                optionTestProduct);
            given(productService.findProductByDisplayedAndShopDisplayed(productIdToOrder2)).willReturn(
                noOptionTestProduct);
            given(additionalDeliveryService.checkAdditionalDeliveryFee("zip code")).willReturn(
                new CheckAdditionalDeliveryFeeOutput(true, false, null));
            given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());
            CreateOrderItemRequest createOrderItemRequest = new CreateOrderItemRequest(productIdToOrder, null,
                productOptionId, productOrderQuantity);
            CreateOrderItemRequest createOrderItemRequest2 = new CreateOrderItemRequest(productIdToOrder2, null,
                null, productOrderQuantity2);

            // when
            Order order = orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser,
                List.of(createOrderItemRequest, createOrderItemRequest2), pointDiscountPrice, tempDeliveryId,
                tempDeliveryMessage);

            // then
            then(pointService).should(times(1)).usePoint(any(User.class), anyLong(), any(Order.class));
            then(productService).should(times(1)).decreaseProductStockCount(anyList());
            then(cartService).should(times(1)).deleteCartItems(anyLong(), anyList());
            if (order.hasCouponDiscountPrice()) {
                then(couponService).should(times(1)).useCoupon(anyList());
            }
        }

        @Nested
        @DisplayName("환불 계좌 관련 validation 테스트")
        class RefundAccountValidationTest {

            @Test
            @DisplayName("Order의 PaymentMethod가 VirtualAccount이면 환불 관련 정보(은행 정보)가 없을 시 예외를 던진다.")
            void crateOrderTestWithRefundInfoWhenPaymentMethodVirtualAccount() {
                // given
                Long pointDiscountPrice = 0L;

                // when // then
                assertThatThrownBy(
                    () -> orderService.createMemberOrder(PaymentMethod.VirtualAccount,
                        getUserWithInvalidRefundBank(), tempCreateOrderItemRequests,
                        pointDiscountPrice, tempDeliveryId, tempDeliveryMessage))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Refund bank information is necessary");
            }

            @Test
            @DisplayName("Order의 PaymentMethod가 VirtualAccount이면 환불 관련 정보(예금주)가 없을 시 예외를 던진다.")
            void crateOrderTestWithRefundInfoWhenPaymentMethodVirtualAccount2() {
                // given
                Bank bank = new Bank();
                bank.setBankName("국민은행");
                bank.setBankCode("11");

                User userWithInvalidRefundBankAccountHolder = new User();
                userWithInvalidRefundBankAccountHolder.setBankAccount("111111111111");
                userWithInvalidRefundBankAccountHolder.setBank(bank);
                Long pointDiscountPrice = 0L;

                // when // then
                assertThatThrownBy(
                    () -> orderService.createMemberOrder(PaymentMethod.VirtualAccount,
                        userWithInvalidRefundBankAccountHolder, tempCreateOrderItemRequests,
                        pointDiscountPrice, tempDeliveryId, tempDeliveryMessage))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Refund bank information is necessary");
            }

            @Test
            @DisplayName("Order의 PaymentMethod가 VirtualAccount이면 환불 관련 정보(은향 계좌)가 없을 시 예외를 던진다.")
            void crateOrderTestWithRefundInfoWhenPaymentMethodVirtualAccount3() {
                // given
                Bank bank = new Bank();
                bank.setBankName("국민은행");
                bank.setBankCode("11");

                User userWithInvalidRefundBankAccount = new User();
                userWithInvalidRefundBankAccount.setBankAccountHolder("예금주");
                userWithInvalidRefundBankAccount.setBank(bank);
                Long pointDiscountPrice = 0L;

                // when
                assertThatThrownBy(
                    () -> orderService.createMemberOrder(PaymentMethod.VirtualAccount,
                        userWithInvalidRefundBankAccount, tempCreateOrderItemRequests,
                        pointDiscountPrice, tempDeliveryId, tempDeliveryMessage))
                    .isInstanceOf(Exception.class)
                    .hasMessage("Refund bank information is necessary");
            }

            private User getUserWithInvalidRefundBank() {
                User user = new User();
                user.setBankAccount("111111111111");
                user.setBankAccountHolder("예금주");

                return new User();
            }
        }
    }
}
