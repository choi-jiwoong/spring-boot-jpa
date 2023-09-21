package com.example.springboot.jpa.user.domain.redisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubService {

    private final RedisTemplate<String, String> redisTemplate;
    public String sendMessage(String message) {
        redisTemplate.convertAndSend("topic1", message);
        return "success"; // 수신여부 모름
    }
}
