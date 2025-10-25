package org.example.resturent.dto.menuitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isGlutenFree;
    private boolean isAvailable;
}
