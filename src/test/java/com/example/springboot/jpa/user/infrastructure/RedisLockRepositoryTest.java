package com.example.springboot.jpa.user.infrastructure;

import com.example.springboot.jpa.user.domain.entity.User;
import com.example.springboot.jpa.user.domain.userService.UserService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
@Transactional
class RedisLockRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;
    @Autowired EntityManager em;

    @BeforeEach
    void setUp_50명의_유저_저장 () {
        List<User> start = userRepository.findAll();
        log.info("최초 회원수 = " + start.size());
        for (int i = 0; i < 50; i++) {
            User toSave = User.builder()
                    .name("이지아" + i)
                    .email("email" + i)
                    .role(User.Role.USER)
                    .build();
            userRepository.save(toSave);
        }
            em.flush();
            em.clear();

        List<User> all = userRepository.findAll();
        log.info("시작 회원수 = " + all.size());
    }

    @Test
    @Transactional
//    @Transactional(Propagation.REQUIRES_NEW)
    void 동시성_한꺼번에_회원_삭제_fail() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

//        for (int i = 1; i < threadCount + 1; i++) {
//            Long j = Long.valueOf(i);
//            userRepository.deleteById(j);
//        }

        for (int i = 1; i < threadCount + 1; i++) {
            Long j = Long.valueOf(i);
            executorService.submit(
                    () -> {
                        try {
                            List<User> all = userRepository.findAll();
                            log.info("현재 user 수 = {}", all.size());
                            // 수가 25이상이면 삭제 -> 아니면 안한다.
                            if (all.size() >= 25) {
                                userRepository.deleteById(j);
                                log.error("user 삭제요청 {}", j);
                            }

                        } finally {
                            latch.countDown();
                        }
                    });
        }
        latch.await();
        List<User> all = userRepository.findAll();
        log.info("남은 회원수 = " + all.size());
        Assertions.assertThat(all.size()).isEqualTo(25);
    }


    @Test
    @Transactional
    void 동시성_한꺼번에_회원_삭제_success() {
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<User> users = userRepository.findAll();
        log.error("=====시작 user====== {}", users.size());


        for (int i = 1; i < 51; i++) {
            Long j = Long.valueOf(i);
            executorService.submit(
                    () -> {
                        userService.deleteUserWithRedis(j);
                    });
            // 스레드 풀 종료
        }
            executorService.shutdown();

        try {
            Thread.sleep(10000); // 충분한 시간 동안 스레드 실행 기다림
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<User> all = userRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(25);
    }
}