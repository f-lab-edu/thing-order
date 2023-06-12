package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUserById(Long id) {
        // TODO : Optional을 리턴하기보다 User 리턴, 없을 시 예외 던지도록 수정 필요
        return userRepository.findById(id);
    }
}
