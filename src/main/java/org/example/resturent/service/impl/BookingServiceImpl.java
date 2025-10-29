package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.response.BookingAvailabilityResponse;
import org.example.resturent.model.TableEntity;
import org.example.resturent.repository.TableRepository;
import org.example.resturent.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final TableRepository tableRepository;

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
}
