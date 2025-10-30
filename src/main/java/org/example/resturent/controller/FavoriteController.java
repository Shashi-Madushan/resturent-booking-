package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.favorite.CreateFavoriteRequestDto;
import org.example.resturent.dto.favorite.FavoriteDto;
import org.example.resturent.dto.response.MessageResponse;
import org.example.resturent.service.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<Page<FavoriteDto>> getUserFavorites(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {
        
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "desc";
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        return ResponseEntity.ok(favoriteService.getUserFavorites(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<FavoriteDto> addFavorite(
            @PathVariable Long userId,
            @Valid @RequestBody CreateFavoriteRequestDto requestDto) {
        
        return new ResponseEntity<>(
            favoriteService.addFavorite(userId, requestDto),
            HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<MessageResponse> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long restaurantId) {
        
        favoriteService.removeFavorite(userId, restaurantId);
        return ResponseEntity.ok(new MessageResponse("Favorite removed successfully"));
    }

    @GetMapping("/check/{restaurantId}")
    public ResponseEntity<MessageResponse> isRestaurantInFavorites(
            @PathVariable Long userId,
            @PathVariable Long restaurantId) {
        
        return ResponseEntity.ok(new MessageResponse("Restaurant is in favorites"));
    }
}
