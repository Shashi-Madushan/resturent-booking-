package org.example.resturent.repository;

import org.example.resturent.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByActiveTrue(Pageable pageable);
    boolean existsByName(String name);
    
    @Query("SELECT r FROM Restaurant r WHERE r.id = :id AND r.active = true")
    Optional<Restaurant> findByIdAndActiveTrue(@Param("id") Long id);
}
