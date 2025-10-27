package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.review.ReviewDTO;
import org.example.resturent.dto.review.ReviewRequestDTO;

import org.example.resturent.exeptions.custom.ResourceNotFoundException;
import org.example.resturent.exeptions.custom.UnauthorizedException;
import org.example.resturent.model.Review;
import org.example.resturent.model.Restaurant;
import org.example.resturent.model.User;
import org.example.resturent.repository.RestaurantRepository;
import org.example.resturent.repository.ReviewRepository;
import org.example.resturent.repository.UserRepository;
import org.example.resturent.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<ReviewDTO> getRestaurantReviews(Long restaurantId, Pageable pageable) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        
        return reviewRepository.findByRestaurantId(restaurantId, pageable)
                .map(review -> {
                    ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
                    dto.setUserId(review.getUser().getId());
                    dto.setRestaurantId(review.getRestaurant().getId());
                    return dto;
                });
    }

    @Override
    @Transactional
    public ReviewDTO createReview(Long userId, ReviewRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        Restaurant restaurant = restaurantRepository.findById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + requestDTO.getRestaurantId()));
        
        // Check if user has already reviewed this restaurant
        if (reviewRepository.existsByUserIdAndRestaurantId(userId, restaurant.getId())) {
            throw new IllegalArgumentException("You have already reviewed this restaurant");
        }
        
        // Validate rating
        if (requestDTO.getRating() == null || requestDTO.getRating() < 1 || requestDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        Review review = Review.builder()
                .user(user)
                .restaurant(restaurant)
                .rating(requestDTO.getRating())
                .comment(requestDTO.getComment())
                .createdAt(OffsetDateTime.now().toLocalDateTime())
                .build();
                
        Review savedReview = reviewRepository.save(review);
        
        // Update restaurant's average rating
        //updateRestaurantRating(restaurant.getId());
        
        return toDTO(savedReview);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long reviewId, Long userId, ReviewRequestDTO requestDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        
        // Check if the review belongs to the user
        if (!review.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this review");
        }
        
        // Validate rating
        if (requestDTO.getRating() != null && (requestDTO.getRating() < 1 || requestDTO.getRating() > 5)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        // Update fields if they are not null in the request
        if (requestDTO.getRating() != null) {
            review.setRating(requestDTO.getRating());
        }
        if (requestDTO.getComment() != null) {
            review.setComment(requestDTO.getComment());
        }
        
        Review updatedReview = reviewRepository.save(review);
        
        // Update restaurant's average rating
        //updateRestaurantRating(review.getRestaurant().getId());
        
        return toDTO(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + reviewId));
        
        // Check if the review belongs to the user or if user is admin
        if (!review.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this review");
        }
        
        Long restaurantId = review.getRestaurant().getId();
        reviewRepository.delete(review);
        
        // Update restaurant's average rating
        //updateRestaurantRating(restaurantId);
    }

    @Override
    public Double getRestaurantAverageRating(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        return reviewRepository.findAverageRatingByRestaurantId(restaurantId);
    }

    @Override
    public Long getRestaurantReviewCount(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        return reviewRepository.countByRestaurantId(restaurantId);
    }

    @Override
    public boolean hasUserReviewedRestaurant(Long userId, Long restaurantId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        return reviewRepository.existsByUserIdAndRestaurantId(userId, restaurantId);
    }
    
/*    private void updateRestaurantRating(Long restaurantId) {
        Double averageRating = reviewRepository.findAverageRatingByRestaurantId(restaurantId);
        Long reviewCount = reviewRepository.countByRestaurantId(restaurantId);
        
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        
        restaurant.setAverageRating(averageRating != null ? averageRating : 0.0);
        restaurant.setReviewCount(reviewCount);
        
        restaurantRepository.save(restaurant);
    }*/
    
    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
        dto.setUserId(review.getUser().getId());
        dto.setRestaurantId(review.getRestaurant().getId());
        return dto;
    }
}
