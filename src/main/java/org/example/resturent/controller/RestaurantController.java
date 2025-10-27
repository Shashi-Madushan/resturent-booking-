package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.resturent.RestaurantDto;
import org.example.resturent.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<Page<RestaurantDto>> getAllRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return ResponseEntity.ok(restaurantService.getAllRestaurants(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantDto> createRestaurant(
            @RequestParam("restaurant") String restaurantJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        RestaurantDto restaurantDto;
        try {
            restaurantDto = objectMapper.readValue(restaurantJson, RestaurantDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid restaurant JSON", e);
        }

        // delegate image handling to service
        return new ResponseEntity<>(
                restaurantService.createRestaurant(restaurantDto, image),
                HttpStatus.CREATED
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantDto> updateRestaurant(
            @PathVariable Long id,
            @RequestParam("restaurant") String restaurantJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        RestaurantDto restaurantDto;
        try {
            restaurantDto = objectMapper.readValue(restaurantJson, RestaurantDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid restaurant JSON", e);
        }

        // delegate image handling to service
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantDto, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
