package org.example.resturent.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private LocalDateTime addedAt;
    private String restaurantName;
    private String restaurantImageUrl;
}
