package org.example.resturent.dto.menuitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {
    private Long id;
    private String itemName;
    private double price;
    private String description;
    private boolean available;
    private String imageUrl;
    private Long restaurantId;
}
