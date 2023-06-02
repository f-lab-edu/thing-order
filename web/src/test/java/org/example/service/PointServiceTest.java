package org.example.service;

import org.example.exception.GraphqlException;
import org.example.repository.PointDetailEventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {
    @Mock
    private PointDetailEventRepository pointDetailEventRepository;
    @InjectMocks
    private PointService pointService;

    @DisplayName("사용자가 자신이 보유한 포인트보다 많은 포인트를 사용하려고 할 시 예외를 던진다.")
    @Test
    void checkUserCouponStatus1() {
        // given
        long userIdHasNotEnoughPoint = 1L;
        Long pointHoldsByUser = 1000L;
        Long pointToUserWantToUse = 2000L;
        given(pointDetailEventRepository.getSumOfTotalUserPoint(userIdHasNotEnoughPoint)).willReturn(pointHoldsByUser);

        // when
        // then
        assertThatThrownBy(() -> pointService.checkUserPoint(userIdHasNotEnoughPoint, pointToUserWantToUse))
                .isInstanceOf(GraphqlException.class)
                .hasMessage("You don't have enough point")
                .satisfies(exception -> {
                    if (exception instanceof GraphqlException) {
                        GraphqlException graphqlException = (GraphqlException) exception;
                        assertThat(graphqlException.getExtensions()).isNotNull();
                        assertThat(graphqlException.getExtensions().get("code")).isEqualTo(
                                "NOT_ENOUGH_POINT");
                    }
                });
    }
}
