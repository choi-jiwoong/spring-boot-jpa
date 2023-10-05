package com.example.springboot.jpa.user.infrastructure;

import com.example.springboot.jpa.user.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

}
