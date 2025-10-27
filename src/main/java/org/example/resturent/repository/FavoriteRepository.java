package org.example.resturent.repository;
import org.example.resturent.model.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.restaurant.id = :restaurantId")
    Optional<Favorite> findByUserIdAndRestaurantId(
        @Param("userId") Long userId, 
        @Param("restaurantId") Long restaurantId
    );
    
    @Query("SELECT COUNT(f) > 0 FROM Favorite f WHERE f.user.id = :userId AND f.restaurant.id = :restaurantId")
    boolean existsByUserIdAndRestaurantId(
        @Param("userId") Long userId,
        @Param("restaurantId") Long restaurantId
    );
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.restaurant.id = :restaurantId")
    void deleteByUserIdAndRestaurantId(
        @Param("userId") Long userId,
        @Param("restaurantId") Long restaurantId
    );
}
