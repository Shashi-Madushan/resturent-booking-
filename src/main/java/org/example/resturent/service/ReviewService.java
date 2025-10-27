package org.example.resturent.service;

import org.example.resturent.dto.review.ReviewDTO;
import org.example.resturent.dto.review.ReviewRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Page<ReviewDTO> getRestaurantReviews(Long restaurantId, Pageable pageable);
    ReviewDTO createReview(Long userId, ReviewRequestDTO requestDTO);
    ReviewDTO updateReview(Long reviewId, Long userId, ReviewRequestDTO requestDTO);
    void deleteReview(Long reviewId, Long userId);
    Double getRestaurantAverageRating(Long restaurantId);
    Long getRestaurantReviewCount(Long restaurantId);
    boolean hasUserReviewedRestaurant(Long userId, Long restaurantId);
}
