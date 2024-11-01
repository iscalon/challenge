package com.card.nico.deposit.layers.core.ports.out;

import com.card.nico.deposit.layers.core.Deposit;

import java.util.List;
import java.util.Optional;

public interface DepositStore {

    Deposit save(Deposit deposit);

    <T extends Deposit> List<T> findAll();

    <T extends Deposit> Optional<T> findById(Long id);

    <T extends Deposit> List<T> findByEmployeeName(String employeeName);

    String type();
}
