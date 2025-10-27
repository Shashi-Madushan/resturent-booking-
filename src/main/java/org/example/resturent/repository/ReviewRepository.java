package org.example.resturent.repository;

import org.example.resturent.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByRestaurantId(Long restaurantId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.restaurant.id = :restaurantId")
    Optional<Review> findByUserIdAndRestaurantId(
        @Param("userId") Long userId, 
        @Param("restaurantId") Long restaurantId
    );
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Long countByRestaurantId(@Param("restaurantId") Long restaurantId);
    
    boolean existsByUserIdAndRestaurantId(Long userId, Long restaurantId);
}
