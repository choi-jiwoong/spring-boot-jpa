package com.example.springboot.jpa.user.domain.userService;

import com.example.springboot.jpa.user.interfaces.UserDto;

public interface UserService {

    void createUser(UserDto dto);

    UserDto getUser(Long userId);

    UserDto updateUser(UserDto user);

    void deleteUser(Long userId);

}
