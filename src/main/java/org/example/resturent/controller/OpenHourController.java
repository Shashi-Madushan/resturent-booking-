package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.OpenHourDto;
import org.example.resturent.service.OpenHourService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/open-hours")
@RequiredArgsConstructor
public class OpenHourController {

    private final OpenHourService openHourService;

    @GetMapping
    public ResponseEntity<Page<OpenHourDto>> getOpenHoursByRestaurantId(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dayOfWeek,asc") String[] sort) {
        
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        return ResponseEntity.ok(openHourService.getOpenHoursByRestaurantId(restaurantId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpenHourDto> getOpenHourById(@PathVariable Long id) {
        return ResponseEntity.ok(openHourService.getOpenHourById(id));
    }

    @PostMapping
    public ResponseEntity<OpenHourDto> createOpenHour(
            @PathVariable Long restaurantId,
            @Valid @RequestBody OpenHourDto openHourDto) {
        
        openHourDto.setRestaurantId(restaurantId);
        return new ResponseEntity<>(
            openHourService.createOpenHour(openHourDto),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpenHourDto> updateOpenHour(
            @PathVariable Long id,
            @Valid @RequestBody OpenHourDto openHourDto) {
        return ResponseEntity.ok(openHourService.updateOpenHour(id, openHourDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpenHour(@PathVariable Long id) {
        openHourService.deleteOpenHour(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<List<OpenHourDto>> updateRestaurantOpenHours(
            @PathVariable Long restaurantId,
            @Valid @RequestBody List<@Valid OpenHourDto> openHourDtos) {
        
        openHourDtos.forEach(dto -> dto.setRestaurantId(restaurantId));
        return ResponseEntity.ok(
            openHourService.updateRestaurantOpenHours(restaurantId, openHourDtos)
        );
    }
}
