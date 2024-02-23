package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.exceptions.InsufficientCompanyBalanceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DepositStrategy {

    Deposit createDeposit(MoneyAmount amount);

    LocalDate expirationDate();

    default LocalDateTime creationDate() {
        return LocalDateTime.now();
    }

    default void assertSufficientCompanyBalance(Company company, MoneyAmount debit) {
        BigDecimal companyBalance = company.balance().amount();
        BigDecimal amount = debit.amount();
        if(companyBalance.compareTo(amount) < 0) {
            throw new InsufficientCompanyBalanceException(company, debit);
        }
    }
}
