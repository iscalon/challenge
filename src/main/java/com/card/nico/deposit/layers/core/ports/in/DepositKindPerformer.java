package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Deposit;

import java.util.List;
import java.util.Optional;

public interface DepositKindPerformer {

    CompanyDepositKindPerformer from(String companyName);

    Optional<Deposit> findById(Long id);

    <T extends Deposit> List<T> findAll();

    <T extends Deposit> List<T> findByEmployeeName(String employeeName);
}
