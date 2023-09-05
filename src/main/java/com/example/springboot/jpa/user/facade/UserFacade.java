package com.example.springboot.jpa.user.facade;

import com.example.springboot.jpa.user.domain.service.UserService;
import com.example.springboot.jpa.user.interfaces.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public void createUser(UserDto dto) {
        userService.createUser(dto);
    }

    public UserDto getUser(Long userId) {
        return userService.getUser(userId);
    }

    public UserDto updateUser(UserDto user) {
        return userService.updateUser(user);
    }

    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }
}
