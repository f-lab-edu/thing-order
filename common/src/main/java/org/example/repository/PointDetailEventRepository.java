package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.entity.PointDetailEvent;
import org.example.entity.PointStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointDetailEventRepository extends JpaRepository<PointDetailEvent, Long> {

    @Query("SELECT SUM(p.amount) FROM PointDetailEvent p WHERE p.user.id = :userId")
    Long getSumOfTotalUserPoint(@Param("userId") long userId);

    @Query("SELECT pointDetailEvent FROM PointDetailEvent pointDetailEvent " +
        "INNER JOIN FETCH pointDetailEvent.user user " +
        "INNER JOIN FETCH pointDetailEvent.pointConstraint pointConstraint " +
        "WHERE user.id = :userId " +
        "AND pointDetailEvent.pointStatus = :pointStatus " +
        "AND pointDetailEvent.isAllUsed IS NULL " +
        "ORDER BY pointDetailEvent.id ASC")
    List<PointDetailEvent> findPointDetailEventsByUserIdAndPointStatus(@Param("userId") Long userId,
        @Param("pointStatus") PointStatus pointStatus);

    @Query("SELECT SUM(pointDetailEvent.amount) FROM PointDetailEvent pointDetailEvent GROUP BY pointDetailEvent.saveId having pointDetailEvent.saveId = :saveId")
    Optional<Integer> getPointUserCanUse(@Param("saveId") Long saveId);

}
