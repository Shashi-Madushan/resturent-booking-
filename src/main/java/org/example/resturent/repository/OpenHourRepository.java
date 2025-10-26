package org.example.resturent.repository;

import org.example.resturent.model.OpenHour;
import org.example.resturent.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface OpenHourRepository extends JpaRepository<OpenHour, Long> {
    Page<OpenHour> findByRestaurantId(Long restaurantId, Pageable pageable);
    List<OpenHour> findByRestaurantId(Long restaurantId);
    boolean existsByRestaurantAndDayOfWeek(Restaurant restaurant, DayOfWeek dayOfWeek);
}
