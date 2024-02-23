package com.card.nico.deposit.layers.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GiftDepositRepository extends JpaRepository<GiftDepositEntity, Long> {

    Optional<GiftDepositEntity> findByCreationDate(LocalDateTime creationDate);

    List<GiftDepositEntity> findByEmployeeName(String name);
}
