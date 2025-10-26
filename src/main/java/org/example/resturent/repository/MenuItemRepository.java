package org.example.resturent.repository;

import org.example.resturent.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    Page<MenuItem> findByRestaurantId(Long restaurantId, Pageable pageable);
    List<MenuItem> findByRestaurantId(Long restaurantId);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MenuItem m WHERE m.restaurant.id = :restaurantId AND LOWER(m.itemName) = LOWER(:itemName)")
    boolean existsByRestaurantIdAndItemNameIgnoreCase(@Param("restaurantId") Long restaurantId, @Param("itemName") String itemName);
    
    Page<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId, Pageable pageable);
}
