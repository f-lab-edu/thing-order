package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.entity.Coupon;
import org.example.entity.CouponConstraint;
import org.example.entity.CouponStatus;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.CouponConstraintRepository;
import org.example.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponConstraintRepository couponConstraintRepository;
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
                    GraphqlException graphqlException = (GraphqlException)exception;
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
        Coupon coupon1 = new Coupon(CouponStatus.Used);
        given(couponRepository.findUserCoupon(testUser.getId(), 1L)).willReturn(
            Optional.of(coupon1));

        // when
        // then
        assertThatThrownBy(() -> couponService.checkUserCouponStatus(testUser, List.of(1L)))
            .isInstanceOf(GraphqlException.class)
            .hasMessage("The Coupon is already used")
            .satisfies(exception -> {
                if (exception instanceof GraphqlException) {
                    GraphqlException graphqlException = (GraphqlException)exception;
                    assertThat(graphqlException.getExtensions()).isNotNull();
                    assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                        "ALREADY_USED_COUPON");
                }
            });
    }

    @DisplayName("쿠폰 사용 시 쿠폰의 상태를 Used로 변경하고, 해당 쿠폰의 캠페인(Constraint)의 use count를 증가시킨다.")
    @Test
    void useCouponTest1() {
        // given

        CouponConstraint couponConstraint1 = new CouponConstraint("테스트 쿠폰 캠페인 1", "설명", true, 0L);
        CouponConstraint couponConstraint2 = new CouponConstraint("테스트 쿠폰 캠페인 2", "설명", true, 0L);

        Coupon coupon1 = new Coupon(false, CouponStatus.Available, couponConstraint1);
        Coupon coupon2 = new Coupon(false, CouponStatus.Available, couponConstraint2);

        // when
        List<Coupon> couponList = couponService.useCoupon(List.of(coupon1, coupon2));

        // then
        assertThat(couponList.size()).isEqualTo(2);

        List<CouponStatus> usedCouponStatusList = couponList.stream()
            .map(coupon -> coupon.getCouponStatus())
            .collect(Collectors.toList());
        assertThat(usedCouponStatusList).containsOnly(CouponStatus.Used);

        List<CouponConstraint> usedCouponConstarintList = couponList.stream()
            .map(coupon -> coupon.getCouponConstraint())
            .collect(Collectors.toList());
        assertThat(usedCouponConstarintList).filteredOn(couponConstraint -> couponConstraint.getUsedCount() == 1);
    }
}
