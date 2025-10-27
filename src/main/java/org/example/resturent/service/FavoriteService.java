package org.example.resturent.service;

import org.example.resturent.dto.favorite.CreateFavoriteRequestDto;
import org.example.resturent.dto.favorite.FavoriteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {
    Page<FavoriteDto> getUserFavorites(Long userId, Pageable pageable);
    FavoriteDto addFavorite(Long userId, CreateFavoriteRequestDto requestDto);
    void removeFavorite(Long userId, Long restaurantId);
    boolean isRestaurantInFavorites(Long userId, Long restaurantId);
}
