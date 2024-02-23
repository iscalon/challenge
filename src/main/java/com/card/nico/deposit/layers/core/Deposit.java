package com.card.nico.deposit.layers.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface Deposit {

    Long getId();
    
    Employee getEmployee();

    Company getCompany();

    MoneyAmount getAmount();

    Optional<LocalDate> getExpirationDate();

    LocalDateTime getCreationDate();

    String getType();
}
