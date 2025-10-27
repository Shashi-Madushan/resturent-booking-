package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.favorite.CreateFavoriteRequestDto;
import org.example.resturent.dto.favorite.FavoriteDto;
import org.example.resturent.exeptions.custom.ResourceNotFoundException;
import org.example.resturent.model.Favorite;
import org.example.resturent.model.Restaurant;
import org.example.resturent.model.User;
import org.example.resturent.repository.FavoriteRepository;
import org.example.resturent.repository.RestaurantRepository;
import org.example.resturent.repository.UserRepository;
import org.example.resturent.service.FavoriteService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<FavoriteDto> getUserFavorites(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        return favoriteRepository.findByUserId(userId, pageable)
                .map(favorite -> {
                    FavoriteDto dto = modelMapper.map(favorite, FavoriteDto.class);
                    dto.setUserId(userId);
                    dto.setRestaurantId(favorite.getRestaurant().getId());
                    return dto;
                });
    }

    @Override
    @Transactional
    public FavoriteDto addFavorite(Long userId, CreateFavoriteRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                
        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + requestDto.getRestaurantId()));
        
        // Check if already favorited
        if (favoriteRepository.existsByUserIdAndRestaurantId(userId, restaurant.getId())) {
            throw new IllegalArgumentException("Restaurant is already in favorites");
        }
        
        Favorite favorite = Favorite.builder()
                .user(user)
                .restaurant(restaurant)
                .createdAt(OffsetDateTime.now())
                .build();
                
        Favorite savedFavorite = favoriteRepository.save(favorite);
        
        FavoriteDto responseDto = modelMapper.map(savedFavorite, FavoriteDto.class);
        responseDto.setUserId(userId);
        responseDto.setRestaurantId(restaurant.getId());
        
        return responseDto;
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long restaurantId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        
        favoriteRepository.deleteByUserIdAndRestaurantId(userId, restaurantId);
    }

    @Override
    public boolean isRestaurantInFavorites(Long userId, Long restaurantId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        
        return favoriteRepository.existsByUserIdAndRestaurantId(userId, restaurantId);
    }
}
