package org.example.repository;

import org.example.config.TestConfig;
import org.example.entity.PointDetailEvent;
import org.example.entity.PointStatus;
import org.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {TestConfig.class, UserRepository.class, PointDetailEventRepository.class},
        properties = "spring" +
                ".config" +
                ".name=application-common-test")
@EnableAutoConfiguration
class PointDetailEventRepositoryTest {
    @Autowired
    private PointDetailEventRepository pointDetailEventRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("userId에 따라서 pointDetailEvent의 총 합을 리턴한다.")
    @Test
    void getSumOfTotalUserPoint() {
        // given
        Long pointToSave1 = 1000L;
        Long pointToSave2 = 2000L;

        User userToSave = User.builder().phoneNumber("01012345678").email("test@gmail.com").name(
                "test").build();
        User savedUser = userRepository.save(userToSave);
        PointDetailEvent pointDetailEventToSave =
                PointDetailEvent.builder().user(savedUser).amount(pointToSave1).build();
        PointDetailEvent pointDetailEventToSave2 =
                PointDetailEvent.builder().user(savedUser).amount(pointToSave2).build();
        pointDetailEventRepository.save(pointDetailEventToSave);
        pointDetailEventRepository.save(pointDetailEventToSave2);

        // when
        Long sumOfTotalUserPoint = pointDetailEventRepository.getSumOfTotalUserPoint(savedUser.getId());

        // then
        assertThat(sumOfTotalUserPoint).isEqualTo(pointToSave1 + pointToSave2);
    }
}
