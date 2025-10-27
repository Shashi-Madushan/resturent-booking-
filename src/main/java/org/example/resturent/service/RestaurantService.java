package org.example.resturent.service;

import org.example.resturent.dto.resturent.RestaurantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
    Page<RestaurantDto> getAllRestaurants(Pageable pageable);
    RestaurantDto getRestaurantById(Long id);
    RestaurantDto createRestaurant(RestaurantDto restaurantDto);
    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);
    void deleteRestaurant(Long id);
}
