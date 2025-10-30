package org.example.resturent.service;

import org.example.resturent.dto.booking.BookingDTO;
import org.example.resturent.dto.booking.BookingRequestDTO;
import org.example.resturent.dto.booking.BookingUpdateDTO;
import org.example.resturent.dto.response.BookingAvailabilityResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingAvailabilityResponse checkAvailability(Long restaurantId, int requiredSeats, LocalDateTime startTime, LocalDateTime endTime);

    BookingDTO createBooking(Long userId, BookingRequestDTO requestDto);

    BookingDTO updateBooking(Long userId, Long bookingId, BookingUpdateDTO updateDto);

    void deleteBooking(Long userId, Long bookingId);

    BookingDTO getBookingForUser(Long userId, Long bookingId);

    List<BookingDTO> getBookingsForUser(Long userId);
}
