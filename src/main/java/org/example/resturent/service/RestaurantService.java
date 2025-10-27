package org.example.resturent.service;

import org.example.resturent.dto.resturent.RestaurantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface RestaurantService {
    Page<RestaurantDto> getAllRestaurants(Pageable pageable);
    RestaurantDto getRestaurantById(Long id);

    // moved image upload responsibility into service
    RestaurantDto createRestaurant(RestaurantDto restaurantDto);
    RestaurantDto createRestaurant(RestaurantDto restaurantDto, MultipartFile image);

    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto);
    RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto, MultipartFile image);

    void deleteRestaurant(Long id);
}
