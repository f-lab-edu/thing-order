package org.example.repository;

import org.example.entity.PointEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointEventRepository extends JpaRepository<PointEvent, Long> {
}
