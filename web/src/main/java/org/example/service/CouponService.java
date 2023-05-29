package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.order.CreateOrderItemRequest;
import org.example.entity.Coupon;
import org.example.entity.CouponStatus;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private CouponRepository couponRepository;

    void checkUserCouponStatus(User user, List<Long> couponIdsToUse) {
        for (Long couponId : couponIdsToUse) {
            Coupon userCoupon = this.couponRepository.findUserCoupon(user.getId(), couponId);

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
}
