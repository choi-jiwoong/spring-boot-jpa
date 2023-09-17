package com.example.springboot.jpa.user.domain.redisService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPubService {

    private final RedisTemplate<String, String> redisTemplate;
    public String sendMessage(String message) {
        try {
            redisTemplate.convertAndSend("topic1", message);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
