package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;


import org.example.resturent.dto.openinghour.OpenHourDto;
import org.example.resturent.exception.ResourceNotFoundException;


import org.example.resturent.model.OpenHour;
import org.example.resturent.model.Restaurant;
import org.example.resturent.repository.OpenHourRepository;
import org.example.resturent.repository.RestaurantRepository;
import org.example.resturent.service.OpenHourService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenHourServiceImpl implements OpenHourService {

    private final OpenHourRepository openHourRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<OpenHourDto> getOpenHoursByRestaurantId(Long restaurantId, Pageable pageable) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
        }
        return openHourRepository.findByRestaurantId(restaurantId, pageable)
                .map(openHour -> modelMapper.map(openHour, OpenHourDto.class));
    }

    @Override
    public OpenHourDto getOpenHourById(Long id) {
        OpenHour openHour = openHourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Open hour not found with id: " + id));
        return modelMapper.map(openHour, OpenHourDto.class);
    }

    @Override
    @Transactional
    public OpenHourDto createOpenHour(OpenHourDto openHourDto) {
        Restaurant restaurant = restaurantRepository.findById(openHourDto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + openHourDto.getRestaurantId()));

        if (openHourRepository.existsByRestaurantAndDayOfWeek(restaurant, openHourDto.getDayOfWeek())) {
            throw new IllegalArgumentException("Open hour already exists for this day of week");
        }

        OpenHour openHour = modelMapper.map(openHourDto, OpenHour.class);
        openHour.setRestaurant(restaurant);
        OpenHour savedOpenHour = openHourRepository.save(openHour);
        return modelMapper.map(savedOpenHour, OpenHourDto.class);
    }

    @Override
    @Transactional
    public OpenHourDto updateOpenHour(Long id, OpenHourDto openHourDto) {
        OpenHour existingOpenHour = openHourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Open hour not found with id: " + id));

        if (!existingOpenHour.getDayOfWeek().equals(openHourDto.getDayOfWeek()) &&
            openHourRepository.existsByRestaurantAndDayOfWeek(
                existingOpenHour.getRestaurant(), 
                openHourDto.getDayOfWeek())) {
            throw new IllegalArgumentException("Open hour already exists for this day of week");
        }

        modelMapper.map(openHourDto, existingOpenHour);
        OpenHour updatedOpenHour = openHourRepository.save(existingOpenHour);
        return modelMapper.map(updatedOpenHour, OpenHourDto.class);
    }

    @Override
    @Transactional
    public void deleteOpenHour(Long id) {
        if (!openHourRepository.existsById(id)) {
            throw new ResourceNotFoundException("Open hour not found with id: " + id);
        }
        openHourRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<OpenHourDto> updateRestaurantOpenHours(Long restaurantId, List<OpenHourDto> openHourDtos) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        // Delete existing open hours
        List<OpenHour> existingOpenHours = openHourRepository.findByRestaurantId(restaurantId);
        openHourRepository.deleteAll(existingOpenHours);

        // Save new open hours
        List<OpenHour> newOpenHours = openHourDtos.stream()
                .map(dto -> {
                    OpenHour openHour = modelMapper.map(dto, OpenHour.class);
                    openHour.setRestaurant(restaurant);
                    return openHour;
                })
                .collect(Collectors.toList());

        List<OpenHour> savedOpenHours = openHourRepository.saveAll(newOpenHours);
        return savedOpenHours.stream()
                .map(openHour -> modelMapper.map(openHour, OpenHourDto.class))
                .collect(Collectors.toList());
    }
}
