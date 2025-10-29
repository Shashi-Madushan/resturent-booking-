package org.example.resturent.dto.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.resturent.enums.BookingStatus;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingFilterDTO {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotNull(message = "Status is required")
    private BookingStatus status;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    @NotBlank(message = "Sort by is required")
    @Pattern(regexp = "date|status", message = "Sort by must be 'date' or 'status'")
    private String sortBy;
    @NotBlank(message = "Order is required")
    @Pattern(regexp = "asc|desc", message = "Order must be 'asc' or 'desc'")
    private String sortOrder;
}