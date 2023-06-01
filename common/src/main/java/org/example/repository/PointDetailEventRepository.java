package org.example.repository;

import org.example.entity.PointDetailEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointDetailEventRepository extends JpaRepository<PointDetailEvent, Long> {
    @Query("SELECT SUM(p.amount) FROM PointDetailEvent p WHERE p.user.id = :userId")
    Long getSumOfTotalUserPoint(@Param("userId") long userId);
}
