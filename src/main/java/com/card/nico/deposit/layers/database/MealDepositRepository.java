package com.card.nico.deposit.layers.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MealDepositRepository extends JpaRepository<MealDepositEntity, Long> {

    Optional<MealDepositEntity> findByCreationDate(LocalDateTime creationDate);

    List<MealDepositEntity> findByEmployeeName(String name);
}
