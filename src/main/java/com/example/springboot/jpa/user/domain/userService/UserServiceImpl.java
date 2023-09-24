package com.example.springboot.jpa.user.domain.userService;

import com.example.springboot.jpa.user.domain.entity.User;
import com.example.springboot.jpa.user.infrastructure.UserRepository;
import com.example.springboot.jpa.user.interfaces.UserDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final RedissonClient redissonClient;

    private final EntityManager em;

    @Override
    @Transactional
    public void createUser(UserDto dto) {
        // user 엔티티 생성
        User toSave = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();

        // 저장
        userRepository.save(toSave);
    }

    @Override
    public UserDto getUser(Long userId) {
        // 없으면 예외발생
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        return UserDto.fromUser(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        user.update(dto.getName(), dto.getEmail());
        return UserDto.fromUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * 동시성을 해결한 delete
     */
    @Override
    @Transactional
    public void deleteUserWithRedis(Long userId) {
        final String threadName = Thread.currentThread().getName(); // 스레드 여러개 생성해서 유저를 삭제한다.
        RLock lock = redissonClient.getLock("userLock");

        try {
            // 락 시도
            boolean isLocked = lock.tryLock(4, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                log.info("LOCK 획득 실패");
            }
            // user 삭제
            List<User> all = userRepository.findAll();
            log.info("지금 전체 user : {} ", all.size());
            if (all.size() >= 25) {
                userRepository.deleteById(userId);
                log.info("threadName : {} / delete삭제 요청: {}", threadName, userId);
            }
        } catch (InterruptedException e) {
            log.info(e.toString());
        } finally {
            lock.unlock();
        }

    }
}
