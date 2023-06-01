package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Coupon;
import org.example.entity.CouponStatus;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.CouponRepository;
import org.example.repository.PointDetailEventRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointDetailEventRepository pointDetailEventRepository;

    void checkUserPoint(Long userId, Long pointAmountToUse) {
        long sumOfTotalUserPoint = this.pointDetailEventRepository.getSumOfTotalUserPoint(userId);

        if (pointAmountToUse > sumOfTotalUserPoint) {
            HashMap<String, Object> ext = new HashMap<String, Object>();
            ext.put("code", "NOT_ENOUGH_POINT");
            throw new GraphqlException("You don't have enough point", ext);
        }
    }
}
