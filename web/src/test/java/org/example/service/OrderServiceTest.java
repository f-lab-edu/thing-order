package org.example.service;

import org.example.domain.DeliveryType;
import org.example.domain.Order;
import org.example.domain.OrderCustomerType;
import org.example.domain.OrderItem;
import org.example.domain.OrderStatus;
import org.example.domain.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class OrderServiceTest {
    static OrderService orderService;

    @BeforeAll
    static void beforeAll() {
        orderService = new OrderService();
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
        void createOrderTest1() {
            Order order = orderService.createMemberOrder();
            Long totalOriginalPrice = order.getTotalOriginalPrice();
            Long sumOfOrderItemTotalAmount = order.getItems().stream().mapToLong(OrderItem::getOrderItemTotalAmount).sum();

            Assertions.assertEquals(totalOriginalPrice, sumOfOrderItemTotalAmount);
        }

        @Test
        @DisplayName("Order의 totalDiscountPrice는 나머지 할인 프로퍼티의 합이다")
        void createOrderTest2() {
            Order order = orderService.createMemberOrder();
            Long totalDiscountPrice = order.getTotalDiscountPrice();
            Long sumOfDiscountPrice = order.getProductDiscountPrice() + order.getCouponDiscountPrice() + order.getPointDiscountPrice();

            Assertions.assertEquals(totalDiscountPrice, sumOfDiscountPrice);
        }

        @Test
        @DisplayName("Order의 deliveryFee는 orderItem의 deliveryFee의 합이다.")
        void createOrderTest3() {
            Order order = orderService.createMemberOrder();
            Long orderDeliveryFee = order.getDeliveryFee();
            Long sumOfDeliveryFee = order.getItems().stream().mapToLong(OrderItem::getDeliveryFee).sum();

            Assertions.assertEquals(orderDeliveryFee, sumOfDeliveryFee);
        }

        @Test
        @DisplayName("Order의 배송지 관련 정보 null check")
        void createOrderTest4() {
            Order order = orderService.createMemberOrder();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(order.getStreetAddress()),
                    () -> Assertions.assertNotNull(order.getDetailAddress()),
                    () -> Assertions.assertNotNull(order.getZipCode()),
                    () -> Assertions.assertNotNull(order.getReceiver()),
                    () -> Assertions.assertNotNull(order.getDeliveryType()),
                    () -> Assertions.assertNotNull(order.getDeliveryMessage()),
                    () -> Assertions.assertNotNull(order.getDeliveryPhoneNumber())
            );
        }

        @Test
        @DisplayName("Order의 주문 고객 정보 null check")
        void createOrderTest5() {
            Order order = orderService.createMemberOrder();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(order.getCustomerEmail()),
                    () -> Assertions.assertNotNull(order.getCustomer()),
                    () -> Assertions.assertNotNull(order.getOrderCustomerType())
            );
        }

        @Test
        @DisplayName("Order의 주문번호, 결제 방식, createdAt, updatedAt null check")
        void createOrderTest6() {
            Order order = orderService.createMemberOrder();

            Assertions.assertAll(
                    () -> Assertions.assertNotNull(order.getOrderNumber()),
                    () -> Assertions.assertNotNull(order.getPaymentMethod()),
                    () -> Assertions.assertNotNull(order.getCreatedAt()),
                    () -> Assertions.assertNotNull(order.getUpdatedAt())
            );
        }

        @Test
        @DisplayName("orderItemTotalPaymentAmount는 orderItemTotalAmount - (productDiscountAmount + couponDiscountAmount + productDiscountAmount) 이다.")
        void createOrderTest7() {
            Order order = orderService.createMemberOrder();

            order.getItems().forEach(orderItem -> {
                Assertions.assertEquals(orderItem.getOrderItemTotalPaymentAmount(),
                        orderItem.getOrderItemTotalAmount() - (orderItem.getProductDiscountAmount() + orderItem.getPointDiscountAmount() + orderItem.getCouponDiscountAmount()));
            });
        }

        @Test
        @DisplayName("orderItem의 상태는 PENDING이어야 한다.")
        void createOrderTest8() {
            Order order = orderService.createMemberOrder();

            order.getItems().forEach(orderItem -> {
                Assertions.assertEquals(orderItem.getOrderStatus(), OrderStatus.PENDING);
            });
        }

        @Test
        @DisplayName("Order의 deliveryType이 Normal이면 각 orderItem의 deliveryFee는 baseShippingFee와 같은 값이다")
        void createOrderTest9() {
            Order order = orderService.createMemberOrder();

            if(order.getDeliveryType() == DeliveryType.NORMAL) {
                order.getItems().forEach(orderItem -> {
                    Assertions.assertEquals(orderItem.getDeliveryFee(), orderItem.getBaseShippingFee());
                });
            }
        }

        @Test
        @DisplayName("Order의 deliveryType이 Jeju면 각 orderItem의 deliveryFee는 baseShippingFee + jejuShippingFee와 같은 값이다")
        void createOrderTest10() {
            Order order = orderService.createMemberOrder();

            if(order.getDeliveryType() == DeliveryType.JEJU) {
                order.getItems().forEach(orderItem -> {
                    Assertions.assertEquals(orderItem.getDeliveryFee(), orderItem.getJejuShippingFee());
                });
            }
        }

        @Test
        @DisplayName("Order의 deliveryType이 Island면 각 orderItem의 deliveryFee는 baseShippingFee + islandShippingFee와 같은 값이다")
        void createOrderTest11() {
            Order order = orderService.createMemberOrder();

            if(order.getDeliveryType() == DeliveryType.ISLAND) {
                order.getItems().forEach(orderItem -> {
                    Assertions.assertEquals(orderItem.getDeliveryFee(), orderItem.getIslandShippingFee());
                });
            }
        }

        @Test
        @DisplayName("Order 객체에서 paymentDate, payment는 null이어야 한다")
        void createdOrderTest12() {
            Order order = orderService.createMemberOrder();

            Assertions.assertAll(
                    () -> Assertions.assertNull(order.getPaymentDate()),
                    () -> Assertions.assertNull(order.getPayment())
            );
        }

        @Test
        @DisplayName("Order의 items 리스트는 널이 아니여야 한다")
        void createOrderTest13() {
            Order order = orderService.createMemberOrder();

            Assertions.assertNotNull(order.getItems());
        }

        @Test
        @DisplayName("Order의 customer 필드는 null이 아니여야 한다")
        void createOrderTest14() {
            Order order = orderService.createMemberOrder();

            Assertions.assertNotNull(order.getCustomer());
        }

        @Test
        @DisplayName("Order의 OrderItem의 product 필드는 null이 아니여야 한다")
        void createOrderTest15() {
            Order order = orderService.createMemberOrder();

            order.getItems().forEach(orderItem -> {
                Assertions.assertNotNull(orderItem.getProduct());
            });
        }

        @Test
        @DisplayName("Order의 OrderItem의 shop 필드는 null이 아니여야 한다")
        void createOrderTest16() {
            Order order = orderService.createMemberOrder();

            order.getItems().forEach(orderItem -> {
                Assertions.assertNotNull(orderItem.getShop());
            });
        }

        @Test
        @DisplayName("Order의 customerType은 MemberOrder여야 한다.")
        void createOrderTest17 () {
            Order order = orderService.createMemberOrder();

            Assertions.assertEquals(order.getOrderCustomerType(), OrderCustomerType.MEMBER_ORDER);
        }

        @Test
        @DisplayName("Order의 PaymentMethod가 Card이면 환불 관련 정보는 null이어야한다.")
        void createOrderTest18() {
            Order order = orderService.createMemberOrder();

            if (order.getPaymentMethod() == PaymentMethod.CARD) {
                Assertions.assertNull(order.getRefundBankAccount());
                Assertions.assertNull(order.getRefundAccountHolder());
                Assertions.assertNull(order.getRefundAccountBankName());
            }
        }

        @Test
        @DisplayName("Order의 PaymentMethod가 VirtualAccount이면 환불 관련 정보는 null이면 안된다..")
        void createOrderTest19() {
            Order order = orderService.createMemberOrder();

            if (order.getPaymentMethod() == PaymentMethod.VIRTUAL_ACCOUNT) {
                Assertions.assertNotNull(order.getRefundBankAccount());
                Assertions.assertNotNull(order.getRefundAccountHolder());
                Assertions.assertNotNull(order.getRefundAccountBankName());
            }
        }

        @Test
        @DisplayName("Order의 totalDiscountPrice(총 할인금액)는 totalOriginalPrice(배송비를 제외한 금액)보다 작거나 같아야 한다.")
        void createOrderTest20() {
            Order order = orderService.createMemberOrder();

            Assertions.assertTrue(order.getTotalDiscountPrice() <= order.getTotalOriginalPrice());
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
    }
}
