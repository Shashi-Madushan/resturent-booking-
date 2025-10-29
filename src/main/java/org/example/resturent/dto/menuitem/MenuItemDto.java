package org.example.resturent.dto.menuitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDto {

    private Long id;
    @NotBlank(message = "Item name is required")
    private String itemNamae;
    @Positive(message = "Price must be greater than 0")
    private double price;
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    private boolean available;
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
    private MultipartFile image;
}