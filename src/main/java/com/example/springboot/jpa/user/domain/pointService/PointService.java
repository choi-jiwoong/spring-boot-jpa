package com.example.springboot.jpa.user.domain.pointService;

import com.example.springboot.jpa.user.domain.entity.Point;
import com.example.springboot.jpa.user.domain.entity.User;
import com.example.springboot.jpa.user.infrastructure.PointRepository;
import com.example.springboot.jpa.user.infrastructure.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;

    private final UserRepository userRepository;

    private final EntityManager em;


    /**
     * 포인트 쌓기
     */
    @Transactional
//    @RedisLock(key = "point")
    public Point insertPoint(Long userId, Long value, long sleep, int i) {

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        User user = userRepository.findById(userId).get();

        Point toSave = Point.builder()
                .value(value)
                .sequence(i)
                .user(user)
                .regDt(LocalDateTime.now())
                .build();

        Point save = pointRepository.save(toSave);
        log.info("===========입금 = {}, {}", save.getUser().getId(), save.getSequence());
        return save;
    }

    /**
     * 포인트 조회하기
     */
    public List<Point> getPointsByUser1(Long userId) {
        return pointRepository.findAll();
    }

    /**
     * 포인트 사용하기
     */
    @Transactional
    public void usePoint(Long userId, Long pointIdx) {
        Optional<Point> byId = pointRepository.findById(pointIdx);

        if (byId.isEmpty()) {
            throw new IllegalArgumentException("해당 포인트가 존재하지 않습니다.");
        }
        pointRepository.delete(byId.get());
    }

    private Integer setSequence(Integer pointSize) {
        if (pointSize == 0) {
            return 1;
        }
        return pointSize + 1;
    }
}
