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
        User userToSave = new User("test@gmail.com", "test user", "01012345678");
        User savedUser = userRepository.save(userToSave);

        CouponConstraint couponConstraintToSave = new CouponConstraint("테스트 쿠폰", "테스트 쿠폰 입니다");
        CouponConstraint savedCouponConstraint = couponConstraintRepository.save(
                couponConstraintToSave);
        Coupon couponToSave = new Coupon(false, CouponStatus.Available, savedCouponConstraint,
                savedUser);
        Coupon savedCoupon = couponRepository.save(couponToSave);

        // when
        Optional<Coupon> coupon = couponRepository.findUserCoupon(savedUser.getId(),
                savedCouponConstraint.getId());

        // then
        coupon.ifPresent(value -> assertThat(value.getId()).isEqualTo(savedCoupon.getId()));
    }
}
