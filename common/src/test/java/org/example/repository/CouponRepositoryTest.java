package org.example.repository;

import org.example.config.TestConfig;
import org.example.entity.Coupon;
import org.example.entity.CouponConstraint;
import org.example.entity.CouponStatus;
import org.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {TestConfig.class, CouponRepository.class, UserRepository.class,
        CouponConstraintRepository.class},
        properties = "spring" +
                ".config" +
                ".name=application-common-test")
@EnableAutoConfiguration
class CouponRepositoryTest {
    @Autowired
    private CouponConstraintRepository couponConstraintRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("user coupon 조회 테스트")
    @Test
    void findUserCouponTest() {
        // given
        User userToSave = User.builder().phoneNumber("01012345678").email("test@gmail.com").name(
                "test").build();
        User savedUser = userRepository.save(userToSave);
        CouponConstraint couponConstraintToSave =
                CouponConstraint.builder().name("테스트 쿠폰").description("테스트 쿠폰 입니다.").build();
        CouponConstraint savedCouponConstraint = couponConstraintRepository.save(couponConstraintToSave);
        Coupon couponToSave =
                Coupon.builder().couponStatus(CouponStatus.Available).user(savedUser).couponConstraint(savedCouponConstraint).isUsed(false).build();
        Coupon savedCoupon = couponRepository.save(couponToSave);

        // when
        Optional<Coupon> coupon = couponRepository.findUserCoupon(savedUser.getId(),
                savedCouponConstraint.getId());

        // then
        assertThat(coupon.get().getId()).isEqualTo(savedCoupon.getId());
    }
}
