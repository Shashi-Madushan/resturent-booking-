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
public class BookingUpdateDTO {
    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
    private Integer partySize;
    private String specialRequests;
    private BookingStatus status;
}
