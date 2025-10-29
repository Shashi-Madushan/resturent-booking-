package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.booking.BookingDTO;
import org.example.resturent.dto.booking.BookingRequestDTO;
import org.example.resturent.dto.booking.BookingUpdateDTO;
import org.example.resturent.dto.response.BookingAvailabilityResponse;
import org.example.resturent.enums.BookingStatus;
import org.example.resturent.exeptions.custom.BadRequestException;
import org.example.resturent.exeptions.custom.ForbiddenException;
import org.example.resturent.exeptions.custom.ResourceNotFoundException;
import org.example.resturent.model.Booking;
import org.example.resturent.model.TableEntity;
import org.example.resturent.model.User;
import org.example.resturent.repository.BookingRepository;
import org.example.resturent.repository.TableRepository;
import org.example.resturent.repository.UserRepository;
import org.example.resturent.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final EnumSet<BookingStatus> ACTIVE_STATUSES = EnumSet.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED,
            BookingStatus.CHECKED_IN
    );

    private final TableRepository tableRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public BookingAvailabilityResponse checkAvailability(Long restaurantId, int requiredSeats,
                                                         LocalDateTime startTime, LocalDateTime endTime) {
        List<TableEntity> availableTables =
                tableRepository.findAvailableTables(restaurantId, startTime, endTime);

        if (availableTables.isEmpty()) {
            return BookingAvailabilityResponse.builder()
                    .status("Not Available")
                    .selectedTables(Collections.emptyList())
                    .totalSeats(0)
                    .build();
        }

        List<TableEntity> selected = findNearestCombination(availableTables, requiredSeats);

        if (selected.isEmpty()) {
            return BookingAvailabilityResponse.builder()
                    .status("Not Available")
                    .selectedTables(Collections.emptyList())
                    .totalSeats(0)
                    .build();
        }

        int totalSeats = selected.stream().mapToInt(TableEntity::getSeats).sum();
        List<Long> ids = selected.stream().map(TableEntity::getId).toList();

        return BookingAvailabilityResponse.builder()
                .status("OK")
                .selectedTables(ids)
                .totalSeats(totalSeats)
                .build();
    }

    @Override
    @Transactional
    public BookingDTO createBooking(Long userId, BookingRequestDTO requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        TableEntity table = tableRepository.findById(requestDto.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + requestDto.getTableId()));

        if (requestDto.getPartySize() == null || requestDto.getPartySize() <= 0) {
            throw new BadRequestException("Party size must be greater than zero.");
        }

        LocalDateTime start = toLocalDateTime(requestDto.getStartDateTime());
        LocalDateTime end = toLocalDateTime(requestDto.getEndDateTime());

        validateTimeRange(start, end);
        ensureTableAvailability(table.getId(), start, end, null);

        Booking booking = Booking.builder()
                .user(user)
                .table(table)
                .startDateTime(start)
                .endDateTime(end)
                .status(BookingStatus.PENDING)
                .partySize(requestDto.getPartySize())
                .specialRequests(requestDto.getSpecialRequests())
                .build();

        Booking saved = bookingRepository.save(booking);
        return toDto(saved);
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(Long userId, Long bookingId, BookingUpdateDTO updateDto) {
        Booking booking = getBookingOrThrow(bookingId);
        ensureOwnership(userId, booking);

        LocalDateTime updatedStart = booking.getStartDateTime();
        LocalDateTime updatedEnd = booking.getEndDateTime();

        if (updateDto.getStartDateTime() != null) {
            updatedStart = toLocalDateTime(updateDto.getStartDateTime());
        }

        if (updateDto.getEndDateTime() != null) {
            updatedEnd = toLocalDateTime(updateDto.getEndDateTime());
        }

        if (updateDto.getStartDateTime() != null || updateDto.getEndDateTime() != null) {
            validateTimeRange(updatedStart, updatedEnd);
            ensureTableAvailability(booking.getTable().getId(), updatedStart, updatedEnd, booking.getId());
            booking.setStartDateTime(updatedStart);
            booking.setEndDateTime(updatedEnd);
        }

        if (updateDto.getPartySize() != null) {
            if (updateDto.getPartySize() <= 0) {
                throw new BadRequestException("Party size must be greater than zero.");
            }
            booking.setPartySize(updateDto.getPartySize());
        }

        if (updateDto.getSpecialRequests() != null) {
            booking.setSpecialRequests(updateDto.getSpecialRequests());
        }

        if (updateDto.getStatus() != null) {
            booking.setStatus(updateDto.getStatus());
        }

        Booking saved = bookingRepository.save(booking);
        return toDto(saved);
    }

    @Override
    @Transactional
    public void deleteBooking(Long userId, Long bookingId) {
        Booking booking = getBookingOrThrow(bookingId);
        ensureOwnership(userId, booking);
        bookingRepository.delete(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDTO getBookingForUser(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        return toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getBookingsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return bookingRepository.findAllByUserIdOrderByStartDateTimeDesc(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Finds the smallest combination of tables where sum(seats) >= requiredSeats.
     * This brute-force method is fine for small restaurant setups.
     */
    private List<TableEntity> findNearestCombination(List<TableEntity> tables, int requiredSeats) {
        List<TableEntity> best = new ArrayList<>();
        int bestTotal = Integer.MAX_VALUE;

        int n = tables.size();
        for (int mask = 1; mask < (1 << n); mask++) {
            List<TableEntity> combo = new ArrayList<>();
            int sum = 0;

            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) != 0) {
                    sum += tables.get(j).getSeats();
                    combo.add(tables.get(j));
                }
            }

            if (sum >= requiredSeats && sum < bestTotal) {
                bestTotal = sum;
                best = combo;
            }
        }

        return best;
    }

    private void ensureTableAvailability(Long tableId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId) {
        boolean hasConflict = bookingRepository.existsConflictingBooking(tableId, startTime, endTime, ACTIVE_STATUSES, excludeBookingId);

        if (hasConflict) {
            throw new BadRequestException("Requested time slot is not available for the selected table.");
        }
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new BadRequestException("Start time and end time are required.");
        }

        if (!startTime.isBefore(endTime)) {
            throw new BadRequestException("Start time must be before end time.");
        }
    }

    private BookingDTO toDto(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .tableId(booking.getTable().getId())
                .startDateTime(toOffsetDateTime(booking.getStartDateTime()))
                .endDateTime(toOffsetDateTime(booking.getEndDateTime()))
                .status(booking.getStatus())
                .partySize(booking.getPartySize())
                .specialRequests(booking.getSpecialRequests())
                .build();
    }

    private LocalDateTime toLocalDateTime(OffsetDateTime dateTime) {
        if (dateTime == null) {
            throw new BadRequestException("Date and time values are required.");
        }
        return dateTime.toLocalDateTime();
    }

    private OffsetDateTime toOffsetDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : OffsetDateTime.of(dateTime, ZoneOffset.UTC);
    }

    private Booking getBookingOrThrow(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
    }

    private void ensureOwnership(Long userId, Booking booking) {
        if (!booking.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to modify this booking.");
        }
    }
}
