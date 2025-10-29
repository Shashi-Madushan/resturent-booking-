package org.example.resturent.repository;

import org.example.resturent.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    Optional<TableEntity> findByLabel(String label);
    boolean existsByLabel(String label);
    boolean existsByLabelAndIdNot(String label, Long id);

    @Query("""
        SELECT t FROM TableEntity t
        WHERE t.restaurant.id = :restaurantId
        AND t.id NOT IN (
            SELECT b.table.id FROM Booking b
            WHERE (b.startDateTime <= :endTime AND b.endDateTime >= :startTime)
            AND b.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN')
        )
        ORDER BY t.seats ASC
    """)
    List<TableEntity> findAvailableTables(
            @Param("restaurantId") Long restaurantId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
