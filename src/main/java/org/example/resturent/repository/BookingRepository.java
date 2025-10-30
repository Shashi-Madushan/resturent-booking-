package org.example.resturent.repository;

import org.example.resturent.enums.BookingStatus;
import org.example.resturent.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserIdOrderByStartDateTimeDesc(Long userId);

    Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);

    @Query("""
        SELECT COUNT(b) > 0 FROM Booking b
        WHERE b.table.id = :tableId
          AND b.status IN :statuses
          AND b.startDateTime < :endTime
          AND b.endDateTime > :startTime
          AND (:excludeId IS NULL OR b.id <> :excludeId)
    """)
    boolean existsConflictingBooking(@Param("tableId") Long tableId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime,
                                     @Param("statuses") Collection<BookingStatus> statuses,
                                     @Param("excludeId") Long excludeId);
}
