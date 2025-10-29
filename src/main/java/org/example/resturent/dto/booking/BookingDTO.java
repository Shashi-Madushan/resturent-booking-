package org.example.resturent.dto.booking;

import jakarta.validation.constraints.NotNull;
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
public class BookingDTO {
    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Table ID is required")
    private Long tableId;

    @NotNull(message = "Start date time is required")
    private OffsetDateTime startDateTime;

    @NotNull(message = "End date time is required")
    private OffsetDateTime endDateTime;

    @NotNull(message = "Status is required")
    private BookingStatus status;

    // if we want additional fields
   /* private String customerName;
    private Integer partySize;
    private String specialRequests;*/
}
