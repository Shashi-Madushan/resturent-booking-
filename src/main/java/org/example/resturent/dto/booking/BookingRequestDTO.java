package org.example.resturent.dto.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    @NotNull(message = "Table ID is required")
    private Long tableId;
    
    @NotNull(message = "Start date time is required")
    @FutureOrPresent(message = "Start date time must be in the future")
    private OffsetDateTime startDateTime;
    
    @NotNull(message = "End date time is required")
    @FutureOrPresent(message = "End date time must be in the future")
    private OffsetDateTime endDateTime;
    
    @NotNull(message = "Party size is required")
    @Positive(message = "Party size must be greater than 0")
    private Integer partySize;
    
    @NotBlank(message = "Special requests is required")
    private String specialRequests;
}