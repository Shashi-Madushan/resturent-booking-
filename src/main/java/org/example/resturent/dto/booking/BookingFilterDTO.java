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
public class BookingFilterDTO {
    private Long userId;
    private BookingStatus status;
    private OffsetDateTime fromDate;
    private OffsetDateTime toDate;
    private String sortBy; // "date", "status"
    private String sortOrder; // "asc", "desc"
}
