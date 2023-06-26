package org.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.example.entity.Order;
import org.example.entity.PointDetailEvent;
import org.example.entity.PointEvent;
import org.example.entity.PointStatus;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.PointConstraintRepository;
import org.example.repository.PointDetailEventRepository;
import org.example.repository.PointEventRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointDetailEventRepository pointDetailEventRepository;
    private final PointEventRepository pointEventRepository;
    private final PointConstraintRepository pointConstraintRepository;

    public void checkUserPoint(Long userId, Long pointAmountToUse) {
        long sumOfTotalUserPoint = this.pointDetailEventRepository.getSumOfTotalUserPoint(userId);

        if (pointAmountToUse > sumOfTotalUserPoint) {
            HashMap<String, Object> ext = new HashMap<String, Object>();
            ext.put("code", "NOT_ENOUGH_POINT");
            throw new GraphqlException("You don't have enough point", ext);
        }
    }

    public long usePoint(User user, Long amountOfPointToUse, Order order) {
        PointEvent pointEvent = new PointEvent(PointStatus.Use, amountOfPointToUse, order, user);
        PointEvent savedPointEvent = this.pointEventRepository.save(pointEvent);

        List<PointDetailEvent> allRemainedPointCanUse = this.pointDetailEventRepository.findPointDetailEventsByUserIdAndPointStatus(
            user.getId(), PointStatus.Save);

        long usedPointInThisOrder = amountOfPointToUse;
        long sum = 0L;

        for (PointDetailEvent pointDetailEvent : allRemainedPointCanUse) {
            if (sum == usedPointInThisOrder) {
                break;
            }

            Optional<Integer> userPointCanUse = this.pointDetailEventRepository.getPointUserCanUse(
                pointDetailEvent.getId());

            Long temp = userPointCanUse.map(integer -> pointDetailEvent.getAmount() - Math.abs(integer))
                .orElseGet(pointDetailEvent::getAmount);

            if (usedPointInThisOrder >= temp) {

                PointDetailEvent newPointDetailEvent = new PointDetailEvent(-temp, pointDetailEvent.getId(),
                    PointStatus.Use, user, pointDetailEvent.getPointConstraint(), savedPointEvent,
                    pointDetailEvent.getOrder());

                this.pointDetailEventRepository.save(newPointDetailEvent);

                pointDetailEvent.setIsAllUsedToTrue();

                pointDetailEvent.getPointConstraint().updateTotalUserPoint(temp);
                this.pointConstraintRepository.save(pointDetailEvent.getPointConstraint());

                usedPointInThisOrder += newPointDetailEvent.getAmount();
                sum += pointDetailEvent.getAmount();
            } else {

                PointDetailEvent newPointDetailEvent = new PointDetailEvent(-usedPointInThisOrder,
                    pointDetailEvent.getId(),
                    PointStatus.Use, user, pointDetailEvent.getPointConstraint(),
                    pointEvent, pointDetailEvent.getOrder());

                this.pointDetailEventRepository.save(newPointDetailEvent);

                pointDetailEvent.getPointConstraint().updateTotalUserPoint(usedPointInThisOrder);
                this.pointConstraintRepository.save(pointDetailEvent.getPointConstraint());

                sum += usedPointInThisOrder;
            }
        }
        return sum;
    }
}
