package com.example.springboot.jpa.user.interfaces;

import com.example.springboot.jpa.user.domain.redisService.RedisPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@Slf4j
@RequiredArgsConstructor
public class RedisController {

    private final RedisPubService redisPubService;

    /**
     * 캐시 확인
     */
    @GetMapping()
    @Cacheable(value = "user") // 캐시에 저장된 이후로는 빨리 반환된다. -> key값도 필요할수도..
    public String get(@RequestParam(value = "id") String id) {
        log.info("get user - userId:{}", id);
        try {
            Thread.sleep(1500); // 파라미터로 넣은 id 값을 1.5초 후에 반환한다.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @PostMapping("/pubsub")
    public String pubSub(@RequestParam String message) {
        return redisPubService.sendMessage(message);
    }

}
