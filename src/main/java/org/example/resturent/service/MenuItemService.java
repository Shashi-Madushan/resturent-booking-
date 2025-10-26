package org.example.resturent.service;

import org.example.resturent.dto.MenuItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuItemService {
    Page<MenuItemDto> getMenuItemsByRestaurantId(Long restaurantId, boolean availableOnly, Pageable pageable);
    MenuItemDto getMenuItemById(Long id);
    MenuItemDto createMenuItem(MenuItemDto menuItemDto);
    MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto);
    void deleteMenuItem(Long id);
    List<MenuItemDto> updateRestaurantMenuItems(Long restaurantId, List<MenuItemDto> menuItemDtos);
}
