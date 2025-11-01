package org.example.resturent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.resturent.dto.response.MessageResponse;
import org.example.resturent.dto.table.TableDTO;
import org.example.resturent.dto.table.TableRequestDTO;
import org.example.resturent.service.TableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<Page<TableDTO>> getAllTables(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
            
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        
        return ResponseEntity.ok(tableService.getAllTables(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableDTO> getTableById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PostMapping
    public ResponseEntity<TableDTO> createTable(@Valid @RequestBody TableRequestDTO tableRequestDTO) {
        return new ResponseEntity<>(
            tableService.createTable(tableRequestDTO),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableDTO> updateTable(
            @PathVariable Long id,
            @Valid @RequestBody TableRequestDTO tableRequestDTO) {
        
        return ResponseEntity.ok(tableService.updateTable(id, tableRequestDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<MessageResponse> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.ok(new MessageResponse("Table deleted successfully"));
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<MessageResponse> isTableAvailable(
            @PathVariable Long id) {
        boolean result = tableService.isTableAvailable(id);
        if (result){
            return ResponseEntity.ok(new MessageResponse("true"));
        }else{
            return ResponseEntity.ok(new MessageResponse("false"));
        }
    }
}
