package org.example.resturent.dto.review;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @DecimalMin(value = "1", message = "Rating must be at least 1")
    @NotNull(message = "Rating is required")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    private String comment;

    @NotNull(message = "Created date is required")
    private OffsetDateTime createdAt;
}