package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.review.ReviewDTO;
import org.example.resturent.dto.review.ReviewRequestDTO;
import org.example.resturent.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Page<ReviewDTO>> getRestaurantReviews(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "desc";
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        return ResponseEntity.ok(reviewService.getRestaurantReviews(restaurantId, pageable));
    }

    @GetMapping("/stats")
    public ResponseEntity<ReviewStatsResponse> getRestaurantReviewStats(
            @PathVariable Long restaurantId) {
        
        Double averageRating = reviewService.getRestaurantAverageRating(restaurantId);
        Long reviewCount = reviewService.getRestaurantReviewCount(restaurantId);
        
        ReviewStatsResponse response = new ReviewStatsResponse(
            averageRating != null ? averageRating : 0.0,
            reviewCount != null ? reviewCount : 0L
        );
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long restaurantId,
            @PathVariable Long userId,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {
        
        // Ensure the restaurantId in the path matches the one in the request body
        if (!restaurantId.equals(requestDTO.getRestaurantId())) {
            throw new IllegalArgumentException("Restaurant ID in path does not match the request body");
        }
        
        return new ResponseEntity<>(
            reviewService.createReview(userId, requestDTO),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long restaurantId,
            @PathVariable Long reviewId,
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody ReviewRequestDTO requestDTO) {
        
        // Ensure the restaurantId in the path matches the one in the request body
        if (!restaurantId.equals(requestDTO.getRestaurantId())) {
            throw new IllegalArgumentException("Restaurant ID in path does not match the request body");
        }
        
        return ResponseEntity.ok(
            reviewService.updateReview(reviewId, userId, requestDTO)
        );
    }

    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(
            @PathVariable Long restaurantId,
            @PathVariable Long reviewId,
            @RequestAttribute("userId") Long userId) {
        
        reviewService.deleteReview(reviewId, userId);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserReviewedRestaurant(
            @PathVariable Long restaurantId,
            @RequestAttribute("userId") Long userId) {
        
        return ResponseEntity.ok(
            reviewService.hasUserReviewedRestaurant(userId, restaurantId)
        );
    }
    
    // Inner class for review statistics response
    private record ReviewStatsResponse(Double averageRating, Long reviewCount) {}
}
