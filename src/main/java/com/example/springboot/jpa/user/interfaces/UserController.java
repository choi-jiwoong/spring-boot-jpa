package com.example.springboot.jpa.user.interfaces;

import com.example.springboot.jpa.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/create")
    public String createUser(@RequestBody UserDto dto) {
        userFacade.createUser(dto);
        return "success";
    }

    @GetMapping("/read")
    public UserDto getUser(@RequestParam Long userId) {
        return userFacade.getUser(userId);
    }

    @PostMapping("/update")
    public UserDto updateUser(@RequestBody UserDto dto) {
        return userFacade.updateUser(dto);
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam Long userId) {
        userFacade.deleteUser(userId);
        return "success";
    }
}
