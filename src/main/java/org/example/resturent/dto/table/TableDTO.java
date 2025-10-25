package org.example.resturent.dto.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO {
    private Long id;
    private String label;
    private int seats;
    private String locationCode;
    private Long restaurantId;
}
