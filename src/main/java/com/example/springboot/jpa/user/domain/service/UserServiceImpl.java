package com.example.springboot.jpa.user.domain.service;

import com.example.springboot.jpa.user.domain.entity.User;
import com.example.springboot.jpa.user.infrastructure.UserRepository;
import com.example.springboot.jpa.user.interfaces.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public void createUser(UserDto dto) {
        // user 엔티티 생성
        User toSave = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();

        // 저장
        userRepository.save(toSave);
    }

    @Override
    public UserDto getUser(Long userId) {
        // 없으면 예외발생
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        return UserDto.fromUser(user);
    }

    @Override
    public UserDto updateUser(UserDto dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        user.update(dto.getName(), dto.getEmail());
        return UserDto.fromUser(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
