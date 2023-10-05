package com.example.springboot.jpa.user.domain.userService;

import com.example.springboot.jpa.user.domain.entity.User;
import com.example.springboot.jpa.user.infrastructure.UserRepository;
import com.example.springboot.jpa.user.interfaces.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    @Transactional
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
    @Transactional
    public UserDto updateUser(UserDto dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        user.update(dto.getName(), dto.getEmail());
        return UserDto.fromUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
