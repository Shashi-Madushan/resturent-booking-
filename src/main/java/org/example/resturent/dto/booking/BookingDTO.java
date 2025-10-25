package org.example.resturent.dto.booking;

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
    private Long id;
    private Long userId;
    private Long tableId;
    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
    private BookingStatus status;

    // if we want additional fields
   /* private String customerName;
    private Integer partySize;
    private String specialRequests;*/
}
