//package com.example.springboot.jpa.user.infrastructure;
//
//import com.example.springboot.jpa.user.domain.entity.User;
//import com.example.springboot.jpa.user.domain.userService.UserService;
//import jakarta.persistence.EntityManager;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.parallel.Execution;
//import org.junit.jupiter.api.parallel.ExecutionMode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@SpringBootTest
//@Slf4j
//@Execution(ExecutionMode.CONCURRENT) // 병렬 실행 어노테이션
//@Transactional
//class RedisLockRepositoryTest {
//
//    @Autowired UserRepository userRepository;
//    @Autowired UserService userService;
//    @Autowired EntityManager em;
//
//    @BeforeEach
//    void setUp_50명의_유저_저장 () {
//
//        // 100 포인트 * 50 번을 한다.
//        // 6000 포인트가 있다. 동시다발적으로 접근하면 결과값이 틀어진다. 접근순서까지 넣기
//        List<User> start = userRepository.findAll();
//        log.info("최초 회원수 = " + start.size());
//        for (int i = 0; i < 50; i++) {
//            User toSave = User.builder()
//                    .name("이지아" + i)
//                    .email("email" + i)
//                    .role(User.Role.USER)
//                    .build();
//            userRepository.save(toSave);
//        }
//            em.flush();
//            em.clear();
//
//        List<User> all = userRepository.findAll();
//        log.info("시작 회원수 = " + all.size());
//    }
//
//    @Test
//    @Transactional
//    // 순차 + 동시성 1) 포인트 혹은 2) 배치(두 개가 동시에 못하게)
//    void 동시성_한꺼번에_회원_삭제_success() {
//        int threadCount = 50;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//        List<User> users = userRepository.findAll();
//        log.error("=====시작 user====== {}", users.size());
//
//
//        for (int i = 1; i < 51; i++) {
//            Long j = Long.valueOf(i);
//            executorService.submit(
//                    () -> {
//                        userService.deleteUserWithRedis(j);
//                    });
//            // 스레드 풀 종료
//        }
//            executorService.shutdown();
//
//        try {
//            Thread.sleep(10000); // 충분한 시간 동안 스레드 실행 기다림
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        List<User> all = userRepository.findAll();
//        Assertions.assertThat(all.size()).isEqualTo(25);
//    }
//}
//// user마다 포인트가 있음 -> 사용하고 쓰는게 순차로 이루어져아한다. -> 만일 이게 섞이면 사용이 안될수도 있다.
//// 그러면 5번 적립 한번 사용 이런식으로 하는데 + 100 사용은 600원씩해 제대로 안되면 순차적으로 되지 않으면 예외생김 -> 동시성 이슈 + 순차적으로 이루어지는 것