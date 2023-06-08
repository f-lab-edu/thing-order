package org.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.entity.Coupon;
import org.example.entity.CouponStatus;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

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
}
