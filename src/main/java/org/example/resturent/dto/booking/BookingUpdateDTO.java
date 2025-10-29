package org.example.resturent.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Future
    @NotNull
    private OffsetDateTime startDateTime;
    
    @Future
    @NotNull
    private OffsetDateTime endDateTime;
    
    @Min(1)
    @Max(20)
    @NotNull
    private Integer partySize;
    
    @Size(max = 255)
    private String specialRequests;
    
    @NotNull
    private BookingStatus status;
}