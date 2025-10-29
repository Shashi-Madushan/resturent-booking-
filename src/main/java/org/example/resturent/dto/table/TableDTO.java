package org.example.resturent.dto.table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {
    private Long id;

    @NotBlank(message = "Label is required")
    @Size(min = 1, max = 50, message = "Label must be between 1 and 50 characters")
    private String label;

    @Min(value = 1, message = "Number of seats must be at least 1")
    private int seats;

    @NotBlank(message = "Location code is required")
    @Size(min = 1, max = 50, message = "Location code must be between 1 and 50 characters")
    private String locationCode;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
}