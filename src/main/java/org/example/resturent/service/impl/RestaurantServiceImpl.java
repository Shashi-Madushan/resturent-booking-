package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.openinghour.OpenHourDto;
import org.example.resturent.dto.resturent.RestaurantDto;
import org.example.resturent.exeptions.custom.ResourceNotFoundException;
import org.example.resturent.model.Restaurant;
import org.example.resturent.model.OpenHour;
import org.example.resturent.repository.RestaurantRepository;
import org.example.resturent.service.RestaurantService;
import org.example.resturent.service.ImageStorageService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final ImageStorageService imageStorageService; // injected to handle uploads

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

    // retain original behavior for callers that don't supply an image
    @Override
    @Transactional
    public RestaurantDto createRestaurant(RestaurantDto restaurantDto) {
        return createRestaurant(restaurantDto, null);
    }

    @Override
    @Transactional
    public RestaurantDto createRestaurant(RestaurantDto restaurantDto, MultipartFile image) {
        if (restaurantRepository.existsByName(restaurantDto.getName())) {
            throw new IllegalArgumentException("Restaurant with this name already exists");
        }

        // handle image upload here
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String imageUrl = imageStorageService.upload(image, fileName);
                restaurantDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        Restaurant restaurant = modelMapper.map(restaurantDto, Restaurant.class);
        restaurant.setActive(true);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return modelMapper.map(savedRestaurant, RestaurantDto.class);
    }

    @Override
    @Transactional
    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto) {
        return updateRestaurant(id, restaurantDto, null);
    }

    @Override
    @Transactional
    public RestaurantDto updateRestaurant(Long id, RestaurantDto restaurantDto, MultipartFile image) {
        Restaurant existingRestaurant = restaurantRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        if (!existingRestaurant.getName().equals(restaurantDto.getName()) &&
            restaurantRepository.existsByName(restaurantDto.getName())) {
            throw new IllegalArgumentException("Restaurant with this name already exists");
        }

        // handle image upload here
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String imageUrl = imageStorageService.upload(image, fileName);
                restaurantDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        // Manually update the fields to avoid collection reference issues
        existingRestaurant.setName(restaurantDto.getName());
        existingRestaurant.setAddress(restaurantDto.getAddress());
        existingRestaurant.setPhone(restaurantDto.getPhone());
        existingRestaurant.setEmail(restaurantDto.getEmail());
        existingRestaurant.setDescription(restaurantDto.getDescription());
        existingRestaurant.setCapacity(restaurantDto.getCapacity());
        
        // Only update image URL if a new one was provided
        if (restaurantDto.getImageUrl() != null && !restaurantDto.getImageUrl().isEmpty()) {
            existingRestaurant.setImageUrl(restaurantDto.getImageUrl());
        }
        
        // Handle openHours collection properly
        if (restaurantDto.getOpenHours() != null && !restaurantDto.getOpenHours().isEmpty()) {
            // Clear existing open hours and add new ones
            existingRestaurant.getOpenHours().clear();
            for (OpenHourDto openHourDto : restaurantDto.getOpenHours()) {
                OpenHour openHour = modelMapper.map(openHourDto, OpenHour.class);
                openHour.setRestaurant(existingRestaurant);
                existingRestaurant.getOpenHours().add(openHour);
            }
        }

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
