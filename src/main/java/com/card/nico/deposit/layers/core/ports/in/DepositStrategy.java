package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DepositStrategy {

    Deposit createDeposit(String companyName, String employeeName, MoneyAmount amount);

    LocalDate expirationDate();

    String depositType();

    default LocalDateTime creationDate() {
        return LocalDateTime.now();
    }
}
