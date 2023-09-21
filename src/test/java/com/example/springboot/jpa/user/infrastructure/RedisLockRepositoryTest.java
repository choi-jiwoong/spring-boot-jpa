package com.example.springboot.jpa.user.infrastructure;

import com.example.springboot.jpa.user.domain.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor
@Transactional
class RedisLockRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;

    @BeforeEach
    void setUp_100명의_유저_저장 () {
        List<User> start = userRepository.findAll();
        log.info("최초 회원수 = " + start.size());
        for (int i = 0; i < 100; i++) {
            User toSave = User.builder()
                    .name("이지아" + i)
                    .email("email" + i)
                    .role(User.Role.USER)
                    .build();
            userRepository.save(toSave);
            em.flush();
            em.clear();
        }
        List<User> all = userRepository.findAll();
        log.info("시작 회원수 = " + all.size());
    }

    @Test
    void 한꺼번에_요청하기() {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < 100; i++) {
            Long j = Long.valueOf(i);
            executorService.submit(
                    () -> {
                        try {
                            userRepository.deleteById(j);
                            em.flush();
                            em.clear();
                        } finally {
                            latch.countDown();
                        }
                    });
        }

        List<User> all = userRepository.findAll();
        log.info("남은 회원수 = " + all.size());

    }

}