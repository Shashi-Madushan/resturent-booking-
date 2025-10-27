package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.menuitem.MenuItemDto;
import org.example.resturent.service.MenuItemService;
import org.example.resturent.service.ImageStorageService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final ImageStorageService imageStorageService;

    @GetMapping
    public ResponseEntity<Page<MenuItemDto>> getMenuItemsByRestaurantId(
            @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "false") boolean availableOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "itemName,asc") String[] sort) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(menuItemService.getMenuItemsByRestaurantId(restaurantId, availableOnly, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getMenuItemById(
            @PathVariable Long restaurantId,
            @PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemDto> createMenuItem(
            @PathVariable Long restaurantId,
            @RequestPart("menuItem") @Valid MenuItemDto menuItemDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        menuItemDto.setRestaurantId(restaurantId);

        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                // Use the MultipartFile upload method from ImageStorageService
                String imageUrl = imageStorageService.upload(image, fileName);
                menuItemDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        return new ResponseEntity<>(menuItemService.createMenuItem(menuItemDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemDto> updateMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long id,
            @RequestPart("menuItem") @Valid MenuItemDto menuItemDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        menuItemDto.setRestaurantId(restaurantId);

        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                // Use the MultipartFile upload method from ImageStorageService
                String imageUrl = imageStorageService.upload(image, fileName);
                menuItemDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<List<MenuItemDto>> updateRestaurantMenuItems(
            @PathVariable Long restaurantId,
            @Valid @RequestBody List<@Valid MenuItemDto> menuItemDtos) {
        menuItemDtos.forEach(dto -> dto.setRestaurantId(restaurantId));
        return ResponseEntity.ok(menuItemService.updateRestaurantMenuItems(restaurantId, menuItemDtos));
    }
}
