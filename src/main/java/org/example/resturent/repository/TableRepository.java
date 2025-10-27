package org.example.resturent.repository;

import org.example.resturent.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    Optional<TableEntity> findByLabel(String label);
    boolean existsByLabel(String label);
    boolean existsByLabelAndIdNot(String label, Long id);
}
