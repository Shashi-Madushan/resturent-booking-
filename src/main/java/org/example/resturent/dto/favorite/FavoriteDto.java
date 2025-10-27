package org.example.resturent.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDto {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private OffsetDateTime createdAt;

}
