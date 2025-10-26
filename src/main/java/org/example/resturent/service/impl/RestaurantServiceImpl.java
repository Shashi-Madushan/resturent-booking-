package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.RestaurantDto;
import org.example.resturent.exception.ResourceNotFoundException;
import org.example.resturent.model.Restaurant;
import org.example.resturent.repository.RestaurantRepository;
import org.example.resturent.service.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<RestaurantDto> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findByActiveTrue(pageable)
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDto.class));
    }

    @Override
    public RestaurantDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        return modelMapper.map(restaurant, RestaurantDto.class);
    }

    @Override
    @Transactional
    public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
        if (restaurantRepository.existsByName(restaurantDto.getName())) {
            throw new IllegalArgumentException("Restaurant with this name already exists");
        }
        
        Restaurant restaurant = modelMapper.map(restaurantDto, Restaurant.class);
        restaurant.setActive(true);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return modelMapper.map(savedRestaurant, RestaurantDto.class);
    }

    @Override
    @Transactional
    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
        Restaurant existingRestaurant = restaurantRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        if (!existingRestaurant.getName().equals(restaurantDto.getName()) && 
            restaurantRepository.existsByName(restaurantDto.getName())) {
            throw new IllegalArgumentException("Restaurant with this name already exists");
        }

        modelMapper.map(restaurantDto, existingRestaurant);
        Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
        return modelMapper.map(updatedRestaurant, RestaurantDto.class);
    }

    @Override
    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        restaurant.setActive(false);
        restaurantRepository.save(restaurant);
    }
}
