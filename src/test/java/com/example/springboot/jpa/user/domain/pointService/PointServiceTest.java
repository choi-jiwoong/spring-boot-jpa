package com.example.springboot.jpa.user.domain.pointService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Execution(ExecutionMode.CONCURRENT) // 병렬 실행 어노테이션
class PointServiceTest {

    @Autowired PointService pointService;
    @Autowired EntityManager em;

    @Test
    @Rollback(false)
    public void insertPoints() {
        for (int i = 0; i < 30; i++) {
            pointService.insertPoint(1L, 100L, 50, i, "method1"); // 3000원이 쌓인다.
        }
    }

    @Test
    @Rollback(false)
    public void insertPoints2() {
        for (int i = 0; i < 30; i++) {
            pointService.insertPoint(1L, 100L, 10, i,  "method2"); // 3000원이 쌓인다.
        }
    }

}