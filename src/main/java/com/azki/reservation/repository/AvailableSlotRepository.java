package com.azki.reservation.repository;

import com.azki.reservation.entity.AvailableSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AvailableSlotRepository extends JpaRepository<AvailableSlot, Long> {
    @Query(value = """
                SELECT *
                FROM available_slots
                WHERE is_reserved = false
                  AND start_time >= NOW()
                ORDER BY start_time ASC
                LIMIT 1
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<AvailableSlot> findNearestAvailableSlot();
}
