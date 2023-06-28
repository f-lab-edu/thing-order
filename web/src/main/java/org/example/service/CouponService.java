package org.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.example.entity.Coupon;
import org.example.entity.CouponConstraint;
import org.example.entity.CouponStatus;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.CouponConstraintRepository;
import org.example.repository.CouponRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponConstraintRepository couponConstraintRepository;

    public void checkUserCouponStatus(User user, List<Long> couponIdsToUse) {
        for (Long couponId : couponIdsToUse) {
            Coupon userCoupon = this.findUserCoupon(user.getId(), couponId);

            if (userCoupon == null) {
                HashMap<String, Object> ext = new HashMap<String, Object>();
                ext.put("code", "INVALID_COUPON");
                throw new GraphqlException("You do not have that coupon", ext);
            }

            if (userCoupon.getCouponStatus() == CouponStatus.Used) {
                HashMap<String, Object> ext = new HashMap<String, Object>();
                ext.put("code", "ALREADY_USED_COUPON");
                throw new GraphqlException("The Coupon is already used", ext);
            }
        }
    }

    public Coupon findUserCoupon(Long userId, Long userCouponId) {
        Optional<Coupon> userCoupon = this.couponRepository.findUserCoupon(userId, userCouponId);

        return userCoupon.orElse(null);
    }

    public List<Coupon> useCoupon(List<Coupon> coupons) {
        List<Coupon> couponList = new ArrayList<>();

        for (Coupon coupon : coupons) {
            coupon.updateCouponStatusToUsed();
            couponList.add(coupon);
        }

        couponRepository.saveAll(couponList);

        this.increaseTotalUseOfCouponConstraint(coupons);

        return couponList;
    }

    private void increaseTotalUseOfCouponConstraint(List<Coupon> coupons) {
        List<CouponConstraint> couponConstraintList = new ArrayList<>();

        for (Coupon coupon : coupons) {
            coupon.getCouponConstraint().increaseTotalUsedCount();
            couponConstraintList.add(coupon.getCouponConstraint());
        }

        couponConstraintRepository.saveAll(couponConstraintList);
    }
}
