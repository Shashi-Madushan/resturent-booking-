package org.example.resturent.dto.table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableRequestDTO {
    @NotBlank(message = "Label is required")
    private String label;
    
    @Min(value = 1, message = "Number of seats must be at least 1")
    private int seats;
    
    private String locationCode;
    
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
}
