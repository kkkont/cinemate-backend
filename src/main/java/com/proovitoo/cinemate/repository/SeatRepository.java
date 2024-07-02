package com.proovitoo.cinemate.repository;

import com.proovitoo.cinemate.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScheduleId(Long scheduleId);
    Seat findBySeatRowAndSeatNumber(int seatRow, int seatNumber);
    Seat findBySeatRowAndSeatNumberAndScheduleId(int seatRow, int seatNumber, Long scheduleId);
}
