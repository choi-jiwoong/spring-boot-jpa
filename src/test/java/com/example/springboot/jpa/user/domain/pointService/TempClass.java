package com.example.springboot.jpa.user.domain.pointService;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Execution(ExecutionMode.CONCURRENT) // 병렬 실행 어노테이션
@Slf4j
public class TempClass {

    @Test
    void test1() {
        for (int i = 0; i < 3; i++) {
            log.info("test1 {}", i);

        }
    }

    @Test
    void test2() {
        for (int i = 0; i < 3; i++) {
            log.info("test2 {}", i);

        }
    }
}
