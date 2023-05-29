package org.example.service;

import org.example.entity.Coupon;
import org.example.entity.CouponStatus;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {
    @Mock
    private CouponRepository couponRepository;
    @InjectMocks
    private CouponService couponService;

    private User testUser;

    @BeforeEach
    void beforeEach() {
        User user = new User();
        user.setId(1L);

        this.testUser = user;
    }

    @DisplayName("해당 user가 가지고 있지 않은 쿠폰을 사용하려고 할 시 예외를 던진다")
    @Test
    void checkUserCouponStatus1() {
        // given
        // when
        // then
        assertThatThrownBy(() -> couponService.checkUserCouponStatus(testUser, List.of(1L, 2L)))
                .isInstanceOf(GraphqlException.class)
                .hasMessage("You do not have that coupon")
                .satisfies(exception -> {
                    if (exception instanceof GraphqlException) {
                        GraphqlException graphqlException = (GraphqlException) exception;
                        assertThat(graphqlException.getExtensions()).isNotNull();
                        assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                                "INVALID_COUPON");
                    }
                });
    }

    @DisplayName("해당 user가 이미 사용한 쿠폰을 사용하려고 할 시 예외를 던진다")
    @Test
    void checkUserCouponStatus2() {
        // given
        Coupon coupon1 = new Coupon();
        coupon1.setCouponStatus(CouponStatus.Used);
        given(couponRepository.findUserCoupon(testUser.getId(), 1L)).willReturn(coupon1);

        // when
        // then
        assertThatThrownBy(() -> couponService.checkUserCouponStatus(testUser, List.of(1L)))
                .isInstanceOf(GraphqlException.class)
                .hasMessage("The Coupon is already used")
                .satisfies(exception -> {
                    if (exception instanceof GraphqlException) {
                        GraphqlException graphqlException = (GraphqlException) exception;
                        assertThat(graphqlException.getExtensions()).isNotNull();
                        assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                                "ALREADY_USED_COUPON");
                    }
                });
    }
}
