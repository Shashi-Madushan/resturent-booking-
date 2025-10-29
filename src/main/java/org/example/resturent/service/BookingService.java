package org.example.resturent.service;

import org.example.resturent.dto.response.BookingAvailabilityResponse;

import java.time.LocalDateTime;

public interface BookingService {
    BookingAvailabilityResponse checkAvailability(Long restaurantId, int requiredSeats, LocalDateTime startTime, LocalDateTime endTime);
}
