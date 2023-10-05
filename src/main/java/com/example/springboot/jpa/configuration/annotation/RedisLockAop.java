package com.example.springboot.jpa.configuration.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(RedisLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);

        String key = REDISSON_LOCK_PREFIX + redisLock.key();
        RLock rLock = redissonClient.getLock(key);  // (1)  LOCK: + key 명으로 락을 잡는다.

        try {
            boolean available = rLock.tryLock(redisLock.waitTime(), redisLock.leaseTime(), redisLock.timeUnit());  // (2)
            if (!available) {
                return false;
            }

            return aopForTransaction.proceed(joinPoint);  // (3) 별도의 트랜잭션 실행해서 돌아간다.
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();   // (4)
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {}", redisLock.key());
            }
        }
    }
}
