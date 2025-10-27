package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;


import org.example.resturent.dto.menuitem.MenuItemDto;
import org.example.resturent.exception.ResourceNotFoundException;

import org.example.resturent.model.MenuItem;
import org.example.resturent.model.Restaurant;
import org.example.resturent.repository.MenuItemRepository;
import org.example.resturent.repository.RestaurantRepository;
import org.example.resturent.service.MenuItemService;
import org.example.resturent.service.ImageStorageService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final ImageStorageService imageStorageService; // handle uploads here

    @Override
    public Page<MenuItemDto> getMenuItemsByRestaurantId(Long restaurantId, boolean availableOnly, Pageable pageable) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        
        Page<MenuItem> menuItems = availableOnly 
            ? menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId, pageable)
            : menuItemRepository.findByRestaurantId(restaurantId, pageable);
            
        return menuItems.map(menuItem -> modelMapper.map(menuItem, MenuItemDto.class));
    }

    @Override
    public MenuItemDto getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        return modelMapper.map(menuItem, MenuItemDto.class);
    }

    // delegate no-image call to new method
    @Override
    @Transactional
    public MenuItemDto createMenuItem(MenuItemDto menuItemDto) {
        return createMenuItem(menuItemDto, null);
    }

    @Override
    @Transactional
    public MenuItemDto createMenuItem(MenuItemDto menuItemDto, MultipartFile image) {
        Restaurant restaurant = restaurantRepository.findById(menuItemDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + menuItemDto.getRestaurantId()));

        if (menuItemRepository.existsByRestaurantIdAndItemNameIgnoreCase(
                restaurant.getId(), menuItemDto.getItemName())) {
            throw new IllegalArgumentException("Menu item with this name already exists in this restaurant");
        }

        // handle image upload here
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String imageUrl = imageStorageService.upload(image, fileName);
                menuItemDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        MenuItem menuItem = modelMapper.map(menuItemDto, MenuItem.class);
        menuItem.setRestaurant(restaurant);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return modelMapper.map(savedMenuItem, MenuItemDto.class);
    }

    // delegate no-image call to new method
    @Override
    @Transactional
    public MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto) {
        return updateMenuItem(id, menuItemDto, null);
    }

    @Override
    @Transactional
    public MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto, MultipartFile image) {
        MenuItem existingMenuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        // Check if the name is being changed and if it conflicts with existing items
        if (!existingMenuItem.getItemName().equalsIgnoreCase(menuItemDto.getItemName()) &&
            menuItemRepository.existsByRestaurantIdAndItemNameIgnoreCase(
                existingMenuItem.getRestaurant().getId(), 
                menuItemDto.getItemName())) {
            throw new IllegalArgumentException("Menu item with this name already exists in this restaurant");
        }

        // handle image upload here
        if (image != null && !image.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String imageUrl = imageStorageService.upload(image, fileName);
                menuItemDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        modelMapper.map(menuItemDto, existingMenuItem);
        MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
        return modelMapper.map(updatedMenuItem, MenuItemDto.class);
    }

    @Override
    @Transactional
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<MenuItemDto> updateRestaurantMenuItems(Long restaurantId, List<MenuItemDto> menuItemDtos) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        // Delete existing menu items
        List<MenuItem> existingMenuItems = menuItemRepository.findByRestaurantId(restaurantId);
        menuItemRepository.deleteAll(existingMenuItems);

        // Save new menu items
        List<MenuItem> newMenuItems = menuItemDtos.stream()
                .map(dto -> {
                    MenuItem menuItem = modelMapper.map(dto, MenuItem.class);
                    menuItem.setRestaurant(restaurant);
                    return menuItem;
                })
                .collect(Collectors.toList());

        List<MenuItem> savedMenuItems = menuItemRepository.saveAll(newMenuItems);
        return savedMenuItems.stream()
                .map(menuItem -> modelMapper.map(menuItem, MenuItemDto.class))
                .collect(Collectors.toList());
    }
}
