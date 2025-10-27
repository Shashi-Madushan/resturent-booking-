package org.example.resturent.dto.openinghour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenHourDto {
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean isClosed;
    private Long restaurantId;
}
