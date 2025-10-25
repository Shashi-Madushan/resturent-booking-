package org.example.resturent.dto.openinghour;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHourRequestDTO {
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;
    
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isClosed;
}
