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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @DisplayName("userService 유효성 검사")
    @Test
    void userServiceDi() {
        assertThat(userService).isNotNull();
    }

    @DisplayName("userId로 user 객체를 조회한다.")
    @Test
    void findUserById() {
        // given
        User user = User.builder().id(1L).email("test@gmail.com").phoneNumber("01012345678").name(
                "test").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        Optional<User> optionalUser = userService.findUserById(1L);

        // then
        assertThat(optionalUser.isPresent()).isEqualTo(true);
        assertThat(optionalUser.get().getId()).isEqualTo(1L);
    }

    @DisplayName("존재하지 않는 id를 조회하면 Optional.isPresent() false를 리턴한다.")
    @Test
    void findUserById2() {
        // given
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // when
        Optional<User> optionalUser = userService.findUserById(2L);

        // then
        assertThat(optionalUser.isPresent()).isEqualTo(false);
    }

}
