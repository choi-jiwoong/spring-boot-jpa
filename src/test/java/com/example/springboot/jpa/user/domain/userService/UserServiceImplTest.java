package com.example.springboot.jpa.user.domain.userService;

import com.example.springboot.jpa.user.domain.entity.User;
import com.example.springboot.jpa.user.infrastructure.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class UserServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void delete_success() {
        // when
        userRepository.deleteById(1L);

        List<User> all = userRepository.findAll();
        org.assertj.core.api.Assertions.assertThat(0).isEqualTo(all.size());
    }

}