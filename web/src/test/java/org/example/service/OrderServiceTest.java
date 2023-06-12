package org.example.service;

import org.example.dto.order.CreateOrderItemRequest;
import org.example.entity.Bank;
import org.example.entity.DeliveryType;
import org.example.entity.Order;
import org.example.entity.OrderCustomerType;
import org.example.entity.OrderItem;
import org.example.entity.OrderStatus;
import org.example.entity.PaymentMethod;
import org.example.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private User mockedUser;
    private Long pointDiscountPrice;
    private List<CreateOrderItemRequest> tempCreateOrderItemRequests;
    @InjectMocks
    private OrderService orderService;
    private PaymentMethod tempPaymentMethod;
    private Long tempDeliveryId;

    @BeforeEach
    void beforeEach() {
        mockedUser = new User();
        CreateOrderItemRequest tempCreateOrderItemRequest = new CreateOrderItemRequest();
        tempCreateOrderItemRequest.setProductId(1L);
        tempCreateOrderItemRequests = List.of(tempCreateOrderItemRequest);
        pointDiscountPrice = 0L;
        tempDeliveryId = 1L;
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
        @Disabled
        void createOrderTest1() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);
            Long totalOriginalPrice = order.getTotalOriginalPrice();
            Long sumOfOrderItemTotalAmount = order.getItems().stream()
                    .mapToLong(OrderItem::getOrderItemTotalAmount).sum();

            assertThat(totalOriginalPrice).isEqualTo(sumOfOrderItemTotalAmount);
        }

        @Test
        @DisplayName("Order의 totalDiscountPrice는 나머지 할인 프로퍼티의 합이다")
        @Disabled
        void createOrderTest2() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);
            Long totalDiscountPrice = order.getTotalDiscountPrice();
            Long sumOfDiscountPrice =
                    order.getProductDiscountPrice() + order.getCouponDiscountPrice()
                            + order.getPointDiscountPrice();

            assertThat(totalDiscountPrice).isEqualTo(sumOfDiscountPrice);
        }

        @Test
        @DisplayName("Order의 deliveryFee는 orderItem의 deliveryFee의 합이다.")
        @Disabled
        void createOrderTest3() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);
            Long orderDeliveryFee = order.getDeliveryFee();
            Long sumOfDeliveryFee = order.getItems().stream().mapToLong(OrderItem::getDeliveryFee)
                    .sum();

            assertThat(orderDeliveryFee).isEqualTo(sumOfDeliveryFee);
        }

        @Test
        @DisplayName("Order의 배송지 관련 정보 null check")
        @Disabled
        void createOrderTest4() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

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
        @Disabled
        void createOrderTest5() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            assertAll(
                    () -> assertThat(order.getCustomerEmail()).isNotNull(),
                    () -> assertThat(order.getCustomer()).isNotNull(),
                    () -> assertThat(order.getOrderCustomerType()).isNotNull()
            );
        }

        @Test
        @DisplayName("Order의 주문번호, 결제 방식, createdAt, updatedAt null check")
        @Disabled
        void createOrderTest6() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            assertAll(
                    () -> assertThat(order.getOrderNumber()).isNotNull(),
                    () -> assertThat(order.getPaymentMethod()).isNotNull(),
                    () -> assertThat(order.getCreatedAt()).isNotNull(),
                    () -> assertThat(order.getUpdatedAt()).isNotNull()
            );
        }

        @Test
        @DisplayName("orderItemTotalPaymentAmount는 orderItemTotalAmount - (productDiscountAmount + couponDiscountAmount + productDiscountAmount) 이다.")
        @Disabled
        void createOrderTest7() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

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
        @Disabled
        void createOrderTest8() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            order.getItems().forEach(orderItem -> assertThat(orderItem.getOrderStatus()).isEqualTo(
                    OrderStatus.Pending));
        }

        @Test
        @DisplayName("Order의 deliveryType이 Normal이면 각 orderItem의 deliveryFee는 baseShippingFee와 같은 값이다")
        @Disabled
        void createOrderTest9() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            if (order.getDeliveryType() == DeliveryType.NORMAL) {
                order.getItems()
                        .forEach(orderItem -> assertThat(orderItem.getDeliveryFee()).isEqualTo(
                                orderItem.getBaseShippingFee()));
            }
        }

        @Test
        @DisplayName("Order의 deliveryType이 Jeju면 각 orderItem의 deliveryFee는 baseShippingFee + jejuShippingFee와 같은 값이다")
        @Disabled
        void createOrderTest10() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            if (order.getDeliveryType() == DeliveryType.JEJU) {
                order.getItems()
                        .forEach(orderItem -> assertThat(orderItem.getDeliveryFee()).isEqualTo(
                                orderItem.getJejuShippingFee()));
            }
        }

        @Test
        @DisplayName("Order의 deliveryType이 Island면 각 orderItem의 deliveryFee는 baseShippingFee + islandShippingFee와 같은 값이다")
        @Disabled
        void createOrderTest11() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            if (order.getDeliveryType() == DeliveryType.ISLAND) {
                order.getItems()
                        .forEach(orderItem -> assertThat(orderItem.getDeliveryFee()).isEqualTo(
                                orderItem.getIslandShippingFee()));
            }
        }

        @Test
        @DisplayName("Order 객체에서 paymentDate는 null이어야 한다")
        @Disabled
        void createdOrderTest12() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            assertAll(
                    () -> assertThat(order.getPaymentDate()).isNull()
            );
        }

        @Test
        @DisplayName("Order의 items 리스트는 널이 아니여야 한다")
        @Disabled
        void createOrderTest13() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            assertThat(order.getItems()).isNotNull();
        }

        @Test
        @DisplayName("Order의 customer 필드는 null이 아니여야 한다")
        @Disabled
        void createOrderTest14() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            assertThat(order.getCustomer()).isNotNull();
        }

        @Test
        @DisplayName("Order의 OrderItem의 product 필드는 null이 아니여야 한다")
        @Disabled
        void createOrderTest15() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            order.getItems().forEach(orderItem -> assertThat(orderItem.getProduct()).isNotNull());
        }

        @Test
        @DisplayName("Order의 OrderItem의 shop 필드는 null이 아니여야 한다")
        @Disabled
        void createOrderTest16() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            order.getItems().forEach(orderItem -> assertThat(orderItem.getShop()).isNotNull());
        }

        @Test
        @DisplayName("Order의 customerType은 MemberOrder여야 한다.")
        @Disabled
        void createOrderTest17() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            assertThat(order.getOrderCustomerType()).isEqualTo(OrderCustomerType.MemberOrder);
        }

        @Test
        @DisplayName("Order의 PaymentMethod가 Card이면 환불 관련 정보는 null이어야한다.")
        @Disabled
        void createOrderTest18() throws Exception {
            Order order = orderService.createMemberOrder(PaymentMethod.Card, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            if (order.getPaymentMethod() == PaymentMethod.Card) {
                assertThat(order.getRefundBankAccount()).isNull();
                assertThat(order.getRefundAccountHolder()).isNull();
                assertThat(order.getRefundAccountBankName()).isNull();
            }
        }

        @Test
        @DisplayName("Order의 PaymentMethod가 VirtualAccount이면 환불 관련 정보는 null이면 안된다..")
        @Disabled
        void createOrderTest19() throws Exception {
            Order order = orderService.createMemberOrder(PaymentMethod.VirtualAccount, mockedUser
                    , tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            if (order.getPaymentMethod() == PaymentMethod.VirtualAccount) {
                assertThat(order.getRefundBankAccount()).isNotNull();
                assertThat(order.getRefundAccountHolder()).isNotNull();
                assertThat(order.getRefundAccountBankName()).isNotNull();
            }
        }

        @Test
        @DisplayName("Order의 totalDiscountPrice(총 할인금액)는 totalOriginalPrice(배송비를 제외한 금액)보다 작거나 같아야 한다.")
        @Disabled
        void createOrderTest20() throws Exception {
            Order order = orderService.createMemberOrder(tempPaymentMethod, mockedUser,
                    tempCreateOrderItemRequests, pointDiscountPrice, tempDeliveryId);

            assertThat(order.getTotalDiscountPrice()).isLessThanOrEqualTo(
                    order.getTotalDiscountPrice());
        }

        @Test
        @DisplayName("Crawling 상품은 주문할 수 없다")
        @Tag("TODO")
        void createOrderTest21() {

            //TODO : implementation this test
        }

        @Test
        @DisplayName("Crawling 상점은 주문할 수 없다")
        @Tag("TODO")
        void createOrderTest22() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("주문하는 상품의 재고가 없을 시 예외를 던져야 한다")
        @Tag("TODO")
        void createOrderTest23() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("주문하는 상품의 isDisplayed 필드는 true여야 하고, 그렇지 않으면 예외를 던진다")
        @Tag("TODO")
        void createOrderTest24() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("주문하는 상점의 isDisplayed 필드는 true여야 하고, 그렇지 않으면 예외를 던진다")
        @Tag("TODO")
        void createOrderTest25() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("주문하는 상품이 존재해야 한다")
        @Tag("TODO")
        void createOrderTest26() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("주문에서 사용하고자 하는 쿠폰의 상태가 Available이여야 한다.")
        @Tag("TODO")
        void createOrderTest27() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("주문에서 포인트를 사용하려면 해당 user의 남은 포인트가 충분해야 한다.")
        @Tag("TODO")
        void createOrderTest28() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("배송지가 제주 지역일 경우 각 상점에서 설정한 제주 배송비가 적용되어야 한다.")
        @Tag("TODO")
        void createOrderTest29() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("배송지가 도서 산간 지역일 경우 각 상점에서 설정한 도서 산간 배송비가 적용되어야 한다.")
        @Tag("TODO")
        void createOrderTest30() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("같은 상점의 상품을 여러개 구매한 경우 하나의 상품에만 배송비가 적용되어야 한다.")
        @Tag("TODO")
        void createOrderTest31() {
            //TODO : implementation this test
        }

        @Test
        @DisplayName("상점에서 조건부 무료 배송 기준을 지정했을 경우, 기준 금액 이상만큼의 상품을 구매했을 시 무료 배송이 적용되어야 한다.")
        @Tag("TODO")
        void createOrderTest32() {
            //TODO : implementation this test
        }

        @Nested
        @DisplayName("환불 계좌 관련 validation 테스트")
        class RefundAccountValidationTest {

            @Test
            @DisplayName("Order의 PaymentMethod가 VirtualAccount이면 환불 관련 정보(은행 정보)가 없을 시 예외를 던진다.")
            void crateOrderTestWithRefundInfoWhenPaymentMethodVirtualAccount() {
                // given

                // when // then
                assertThatThrownBy(
                        () -> orderService.createMemberOrder(PaymentMethod.VirtualAccount,
                                getUserWithInvalidRefundBank(), tempCreateOrderItemRequests,
                                pointDiscountPrice, tempDeliveryId))
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

                // when // then
                assertThatThrownBy(
                        () -> orderService.createMemberOrder(PaymentMethod.VirtualAccount,
                                userWithInvalidRefundBankAccountHolder, tempCreateOrderItemRequests,
                                pointDiscountPrice, tempDeliveryId))
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

                // when
                assertThatThrownBy(
                        () -> orderService.createMemberOrder(PaymentMethod.VirtualAccount,
                                userWithInvalidRefundBankAccount, tempCreateOrderItemRequests,
                                pointDiscountPrice, tempDeliveryId))
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
