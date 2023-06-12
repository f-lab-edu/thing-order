package org.example.repository;

import org.example.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("select c from Coupon c inner join User u on u.id = c.user.id inner join " +
            "CouponConstraint cc on c.couponConstraint.id = cc.id where u.id = :userId and cc.id = :couponId")
    Optional<Coupon> findUserCoupon(@Param("userId") long userId,
                                    @Param("couponId") Long userCouponId);
}
