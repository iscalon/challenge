package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DepositStrategy {

    Deposit createDeposit(String companyName, String employeeName, MoneyAmount amount);

    LocalDate expirationDate();

    String depositType();

    default LocalDateTime creationDate() {
        return LocalDateTime.now();
    }

    <T extends Deposit> Optional<T> findById(Long id);

    <T extends Deposit> List<T> findAll();

    <T extends Deposit> List<T> findByEmployeeName(String employeeName);

    Deposit save(Deposit deposit);
}
