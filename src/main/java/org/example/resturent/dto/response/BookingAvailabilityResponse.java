package org.example.resturent.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingAvailabilityResponse {
    private String status;
    private List<Long> selectedTables;
    private int totalSeats;
}