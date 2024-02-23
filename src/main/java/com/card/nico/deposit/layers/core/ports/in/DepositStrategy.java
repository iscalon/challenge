package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.exceptions.DepositCoreException;
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

    default void assertSufficientAmountAndCompanyBalance(Company company, MoneyAmount debit) {
        BigDecimal amount = debit.amount();
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DepositCoreException("Amount must be greater than 0");
        }
        BigDecimal companyBalance = company.balance().amount();
        if(companyBalance.compareTo(amount) < 0) {
            throw new InsufficientCompanyBalanceException(company, debit);
        }
    }
}
