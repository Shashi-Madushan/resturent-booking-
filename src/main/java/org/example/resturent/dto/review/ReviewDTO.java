package org.example.resturent.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long restaurantId;
    private String restaurantName;
    private Integer rating;
    private String comment;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
