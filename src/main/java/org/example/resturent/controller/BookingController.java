package org.example.resturent.controller;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.response.BookingAvailabilityResponse;
import org.example.resturent.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/check")
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
}
