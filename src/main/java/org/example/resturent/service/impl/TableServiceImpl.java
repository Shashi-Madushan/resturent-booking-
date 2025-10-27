package org.example.resturent.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.table.TableDTO;
import org.example.resturent.dto.table.TableRequestDTO;

import org.example.resturent.exeptions.custom.ResourceAlreadyExistsException;
import org.example.resturent.exeptions.custom.ResourceNotFoundException;
import org.example.resturent.model.Restaurant;
import org.example.resturent.model.TableEntity;
import org.example.resturent.repository.RestaurantRepository;
import org.example.resturent.repository.TableRepository;
import org.example.resturent.service.TableService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<TableDTO> getAllTables(Pageable pageable) {
        return tableRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public TableDTO getTableById(Long id) {
        TableEntity table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
        return convertToDTO(table);
    }

    @Override
    @Transactional
    public TableDTO createTable(TableRequestDTO tableRequestDTO) {
        // Check if table with the same label already exists
        if (tableRepository.existsByLabel(tableRequestDTO.getLabel())) {
            throw new ResourceAlreadyExistsException("Table with label " + tableRequestDTO.getLabel() + " already exists");
        }

        // Get the restaurant
        Restaurant restaurant = restaurantRepository.findById(tableRequestDTO.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + tableRequestDTO.getRestaurantId()));

        // Create and save the table
        TableEntity table = TableEntity.builder()
                .label(tableRequestDTO.getLabel())
                .seats(tableRequestDTO.getSeats())
                .locationCode(tableRequestDTO.getLocationCode())
                .restaurant(restaurant)
                .build();

        TableEntity savedTable = tableRepository.save(table);
        return convertToDTO(savedTable);
    }

    @Override
    @Transactional
    public TableDTO updateTable(Long id, TableRequestDTO tableRequestDTO) {
        TableEntity existingTable = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));

        // Check if another table with the same label exists (excluding current table)
        if (tableRepository.existsByLabelAndIdNot(tableRequestDTO.getLabel(), id)) {
            throw new ResourceAlreadyExistsException("Another table with label " + tableRequestDTO.getLabel() + " already exists");
        }

        // Update fields if they are not null in the request
        if (tableRequestDTO.getLabel() != null) {
            existingTable.setLabel(tableRequestDTO.getLabel());
        }
        if (tableRequestDTO.getSeats() > 0) {
            existingTable.setSeats(tableRequestDTO.getSeats());
        }
        if (tableRequestDTO.getLocationCode() != null) {
            existingTable.setLocationCode(tableRequestDTO.getLocationCode());
        }
        
        // Update restaurant if provided
        if (tableRequestDTO.getRestaurantId() != null && 
            !Objects.equals(existingTable.getRestaurant().getId(), tableRequestDTO.getRestaurantId())) {
            Restaurant restaurant = restaurantRepository.findById(tableRequestDTO.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + tableRequestDTO.getRestaurantId()));
            existingTable.setRestaurant(restaurant);
        }

        TableEntity updatedTable = tableRepository.save(existingTable);
        return convertToDTO(updatedTable);
    }

    @Override
    @Transactional
    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table not found with id: " + id);
        }
        
        // Check for active reservations before deleting
        // This is a placeholder - implement actual reservation check if needed
        // if (reservationRepository.existsByTableIdAndStatusIn(id, List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED))) {
        //     throw new BusinessException("Cannot delete table with active or upcoming reservations");
        // }
        
        tableRepository.deleteById(id);
    }

    @Override
    public boolean isTableAvailable(Long tableId) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + tableId));
        
        // This is a simplified check - you might want to implement actual availability logic
        // based on reservations, opening hours, etc.
        return true;
    }
    
    private TableDTO convertToDTO(TableEntity table) {
        TableDTO dto = modelMapper.map(table, TableDTO.class);
        if (table.getRestaurant() != null) {
            dto.setRestaurantId(table.getRestaurant().getId());
        }
        return dto;
    }
}
