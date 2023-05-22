package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @DisplayName("userId로 user 객체를 조회한다.")
    @Test
    void findUserById() {
        // given
        long userId = 1L;
        User user = User.builder().id(userId).email("test@gmail.com").phoneNumber("01012345678").name(
                "test").build();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when
        Optional<User> optionalUser = userService.findUserById(userId);

        // then
        verify(userRepository, times(1)).findById(userId);
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        assertThat(optionalUser.get().getId()).isEqualTo(userId);
    }

    @DisplayName("존재하지 않는 id를 조회하면 Optional.isPresent() false를 리턴한다.")
    @Test
    void findUserById2() {
        // given
        long invalidUserId = 2L;
        given(userRepository.findById(invalidUserId)).willReturn(Optional.empty());

        // when
        Optional<User> optionalUser = userService.findUserById(invalidUserId);

        // then
        verify(userRepository, times(1)).findById(invalidUserId);
        assertThat(optionalUser.isPresent()).isEqualTo(false);
    }
}
