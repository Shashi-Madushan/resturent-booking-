package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.booking.BookingDTO;
import org.example.resturent.dto.booking.BookingRequestDTO;
import org.example.resturent.dto.booking.BookingUpdateDTO;
import org.example.resturent.dto.response.BookingAvailabilityResponse;
import org.example.resturent.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/bookings/check")
    public ResponseEntity<BookingAvailabilityResponse> checkAvailability(
            @RequestParam Long restaurantId,
            @RequestParam int requiredSeats,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        BookingAvailabilityResponse response = bookingService.checkAvailability(
                restaurantId, requiredSeats, start, end
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/bookings")
    public ResponseEntity<BookingDTO> createBooking(@PathVariable Long userId,
                                                    @Valid @RequestBody BookingRequestDTO requestDto) {
        BookingDTO booking = bookingService.createBooking(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/users/{userId}/bookings")
    public ResponseEntity<List<BookingDTO>> getBookingsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsForUser(userId));
    }

    @GetMapping("/users/{userId}/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingForUser(@PathVariable Long userId,
                                                        @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingForUser(userId, bookingId));
    }

    @PutMapping("/users/{userId}/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long userId,
                                                    @PathVariable Long bookingId,
                                                    @Valid @RequestBody BookingUpdateDTO updateDto) {
        BookingDTO booking = bookingService.updateBooking(userId, bookingId, updateDto);
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/users/{userId}/bookings/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long userId,
                              @PathVariable Long bookingId) {
        bookingService.deleteBooking(userId, bookingId);
    }
}
