package org.example.resturent.service;

import org.example.resturent.dto.menuitem.MenuItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuItemService {
    Page<MenuItemDto> getMenuItemsByRestaurantId(Long restaurantId, boolean availableOnly, Pageable pageable);
    MenuItemDto getMenuItemById(Long id);

    // moved image upload responsibility into service layer
    MenuItemDto createMenuItem(MenuItemDto menuItemDto);
    MenuItemDto createMenuItem(MenuItemDto menuItemDto, MultipartFile image);

    MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto);
    MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto, MultipartFile image);

    void deleteMenuItem(Long id);
    List<MenuItemDto> updateRestaurantMenuItems(Long restaurantId, List<MenuItemDto> menuItemDtos);
}
