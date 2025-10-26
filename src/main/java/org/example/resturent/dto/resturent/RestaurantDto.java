package org.example.resturent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private int capacity;
    private String imageUrl;
    private boolean active;
    private List<OpenHourDto> openHours;
}
