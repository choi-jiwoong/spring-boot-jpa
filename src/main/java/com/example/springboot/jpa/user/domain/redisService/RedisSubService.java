package com.example.springboot.jpa.user.domain.redisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
// MessageListener를 구현해서 subscribe를 구현한다.
public class RedisSubService implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 메세지를 받고 나서의 로직
        log.error("message subscribe");
        System.out.println("받은 메시지 = " + message.toString());
    }
}
