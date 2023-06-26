package org.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.example.config.TestConfig;
import org.example.entity.DiscountType;
import org.example.entity.PointConstraint;
import org.example.entity.PointDetailEvent;
import org.example.entity.PointEvent;
import org.example.entity.PointStatus;
import org.example.entity.PointTarget;
import org.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TestConfig.class, PointDetailEventRepository.class, PointEventRepository.class,
    PointConstraintRepository.class, UserRepository.class}, properties = "spring.config"
    + ".name=application-common-test")
@EnableAutoConfiguration
class PointDetailEventRepositoryTest {
    @Autowired
    private PointDetailEventRepository pointDetailEventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointConstraintRepository pointConstraintRepository;

    @Autowired
    private PointEventRepository pointEventRepository;

    @DisplayName("user의 PointDetailEvent를 상태별로 조회한다.")
    @Test
    void findPointDetailEventsByUserIdAndPointStatus() {
        // given
        User userToSave = new User("test@naver.com", "temp user", "010-9900-7278");

        User savedUser = this.userRepository.save(userToSave);

        PointConstraint pointConstraint = new PointConstraint("point test", "point test", true, true, 1000L,
            DiscountType.Value, PointTarget.Manual, 0L, 0L);

        PointConstraint savedPointConstraint = this.pointConstraintRepository.save(pointConstraint);

        PointEvent pointEvent = new PointEvent(PointStatus.Save, 1000L, userToSave, savedPointConstraint);

        PointEvent savedPointEvent = this.pointEventRepository.save(pointEvent);

        PointDetailEvent pointDetailEvent = new PointDetailEvent(1000L, savedPointEvent.getId(), PointStatus.Save,
            savedUser, savedPointConstraint, pointEvent);

        PointDetailEvent saved = this.pointDetailEventRepository.save(pointDetailEvent);
        // when

        List<PointDetailEvent> pointDetailEventsByUserIdAndPointStatus = this.pointDetailEventRepository.findPointDetailEventsByUserIdAndPointStatus(
            savedUser.getId(),
            PointStatus.Save);

        Long sumOfTotalUserPoint = this.pointDetailEventRepository.getSumOfTotalUserPoint(savedUser.getId());

        Optional<Integer> pointUserCanUse = this.pointDetailEventRepository.getPointUserCanUse(pointEvent.getId());

        // then
        assertThat(pointDetailEventsByUserIdAndPointStatus).isNotNull();
        assertThat(sumOfTotalUserPoint).isEqualTo(1000L);
        assertThat(pointUserCanUse.get()).isEqualTo(1000L);
    }
}
