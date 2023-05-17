package org.example.repository;

import org.example.entity.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("user의 id에 따라 user 객체를 리턴한다.")
    @Disabled
    @Test
    void findUserWithId() {
        // given
        User user1 = User.builder().phoneNumber("01012345678").email("test@gmail.com").name(
                "test").build();
        User savedUser = userRepository.save(user1);

        // when
        Optional<User> optionalUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        assertThat(optionalUser.get().getId()).isEqualTo(savedUser.getId());
    }

}
