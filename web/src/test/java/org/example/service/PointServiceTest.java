package org.example.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.example.entity.DiscountType;
import org.example.entity.PointConstraint;
import org.example.entity.PointDetailEvent;
import org.example.entity.PointEvent;
import org.example.entity.PointStatus;
import org.example.entity.PointTarget;
import org.example.entity.User;
import org.example.exception.GraphqlException;
import org.example.repository.PointConstraintRepository;
import org.example.repository.PointDetailEventRepository;
import org.example.repository.PointEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointConstraintRepository pointConstraintRepository;
    @Mock
    private PointDetailEventRepository pointDetailEventRepository;
    @Mock
    private PointEventRepository pointEventRepository;
    @InjectMocks
    private PointService pointService;

    @DisplayName("사용자가 자신이 보유한 포인트보다 많은 포인트를 사용하려고 할 시 예외를 던진다.")
    @Test
    void checkUserCouponStatus1() {
        // given
        long userIdHasNotEnoughPoint = 1L;
        Long pointHoldsByUser = 1000L;
        Long pointToUserWantToUse = 2000L;
        given(pointDetailEventRepository.getSumOfTotalUserPoint(
            userIdHasNotEnoughPoint)).willReturn(pointHoldsByUser);

        // when
        // then
        assertThatThrownBy(
            () -> pointService.checkUserPoint(userIdHasNotEnoughPoint, pointToUserWantToUse))
            .isInstanceOf(GraphqlException.class)
            .hasMessage("You don't have enough point")
            .satisfies(exception -> {
                if (exception instanceof GraphqlException) {
                    GraphqlException graphqlException = (GraphqlException)exception;
                    assertThat(graphqlException.getExtensions()).isNotNull();
                    assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                        "NOT_ENOUGH_POINT");
                }
            });
    }

    @DisplayName("포인트를 사용하면 포인트 사용 총 사용량을 리턴한다.")
    @Test
    void usePoint1() {
        // given
        User userToSave = new User("test@naver.com", "temp user", "010-9900-7278");
        userToSave.setId(88293L);

        PointConstraint pointConstraint1 = new PointConstraint("point test1", "point test1", true, true, 1000L,
            DiscountType.Value, PointTarget.Manual, 0L, 0L);
        PointConstraint pointConstraint2 = new PointConstraint("point test2", "point test2", true, true, 119L,
            DiscountType.Value, PointTarget.Manual, 0L, 0L);

        PointEvent pointEvent1 = new PointEvent(PointStatus.Save, pointConstraint1.getDiscount(), userToSave,
            pointConstraint1);
        pointEvent1.setId(1L);
        PointEvent pointEvent2 = new PointEvent(PointStatus.Save, pointConstraint2.getDiscount(), userToSave,
            pointConstraint2);
        pointEvent2.setId(2L);

        PointDetailEvent pointDetailEvent1 = new PointDetailEvent(pointConstraint1.getDiscount(), pointEvent1.getId(),
            PointStatus.Save, userToSave, pointConstraint1, pointEvent1);
        PointDetailEvent pointDetailEvent2 = new PointDetailEvent(pointConstraint2.getDiscount(), pointEvent2.getId(),
            PointStatus.Save, userToSave, pointConstraint2, pointEvent2);

        PointEvent pointEventToUse = new PointEvent(PointStatus.Use, 500L, null, userToSave);
        pointEventToUse.setId(100L);
        given(pointEventRepository.save(Mockito.any(PointEvent.class))).willReturn(pointEventToUse);

        given(pointDetailEventRepository.findPointDetailEventsByUserIdAndPointStatus(userToSave.getId(),
            PointStatus.Save)).willReturn(List.of(pointDetailEvent1, pointDetailEvent2));

        given(pointDetailEventRepository.getPointUserCanUse(pointDetailEvent1.getId())).willReturn(Optional.empty());
        given(pointDetailEventRepository.getPointUserCanUse(pointDetailEvent2.getId())).willReturn(Optional.empty());

        // when
        long usedPoint = pointService.usePoint(userToSave, 500L, null);

        // then
        assertThat(usedPoint).isEqualTo(500L);
    }
}
