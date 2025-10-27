package org.example.resturent.dto.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFavoriteRequestDto {
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
}
