package org.example.resturent.service;

import org.example.resturent.dto.table.TableDTO;
import org.example.resturent.dto.table.TableRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TableService {
    Page<TableDTO> getAllTables(Pageable pageable);
    TableDTO getTableById(Long id);
    TableDTO createTable(TableRequestDTO tableRequestDTO);
    TableDTO updateTable(Long id, TableRequestDTO tableRequestDTO);
    void deleteTable(Long id);
    boolean isTableAvailable(Long tableId);
}
