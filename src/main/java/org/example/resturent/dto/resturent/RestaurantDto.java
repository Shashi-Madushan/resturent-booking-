package org.example.resturent.dto.resturent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.resturent.dto.openinghour.OpenHourDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @Min(value = 1, message = "Capacity must be positive")
    private int capacity;
    
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    @NotNull(message = "Active is required")
    private boolean active;
    private List<OpenHourDto> openHours;
}