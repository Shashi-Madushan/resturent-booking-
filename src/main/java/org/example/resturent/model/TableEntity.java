package org.example.resturent.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurant_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private int seats;
    private String locationCode;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
