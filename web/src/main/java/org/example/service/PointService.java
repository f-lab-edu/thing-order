package org.example.service;

import java.util.HashMap;

import org.example.exception.GraphqlException;
import org.example.repository.PointDetailEventRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointDetailEventRepository pointDetailEventRepository;

    public void checkUserPoint(Long userId, Long pointAmountToUse) {
        long sumOfTotalUserPoint = this.pointDetailEventRepository.getSumOfTotalUserPoint(userId);

        if (pointAmountToUse > sumOfTotalUserPoint) {
            HashMap<String, Object> ext = new HashMap<String, Object>();
            ext.put("code", "NOT_ENOUGH_POINT");
            throw new GraphqlException("You don't have enough point", ext);
        }
    }
}
