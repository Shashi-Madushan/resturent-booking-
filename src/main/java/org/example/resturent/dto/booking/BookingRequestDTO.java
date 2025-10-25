package org.example.resturent.dto.booking;

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
    private Long tableId;
    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
    private Integer partySize;
    private String specialRequests;
}
