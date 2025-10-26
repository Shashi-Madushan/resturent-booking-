package org.example.resturent.service;

import org.example.resturent.dto.OpenHourDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OpenHourService {
    Page<OpenHourDto> getOpenHoursByRestaurantId(Long restaurantId, Pageable pageable);
    OpenHourDto getOpenHourById(Long id);
    OpenHourDto createOpenHour(OpenHourDto openHourDto);
    OpenHourDto updateOpenHour(Long id, OpenHourDto openHourDto);
    void deleteOpenHour(Long id);
    List<OpenHourDto> updateRestaurantOpenHours(Long restaurantId, List<OpenHourDto> openHourDtos);
}
