package org.example.repository;

import org.example.config.TestConfig;
import org.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TestConfig.class,
        UserRepository.class}, properties = "spring.config.name=application-common-test")
@EnableAutoConfiguration
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("user의 id에 따라 user 객체를 리턴한다.")
    @Test
    void findUserWithId() {
        // given
        User user1 = new User("test@gmail.com", "test user", "01012345678");
        User savedUser = userRepository.save(user1);

        // when
        Optional<User> optionalUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(savedUser.getId()));
    }
}
