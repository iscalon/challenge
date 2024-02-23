package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.*;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public class GiftDepositStrategy implements DepositStrategy {

    private final Company company;
    private final Employee employee;

    public GiftDepositStrategy(Company company, Employee employee) {
        this.company = requireNonNull(company);
        this.employee = requireNonNull(employee);
    }

    @Override
    public Deposit createDeposit(MoneyAmount amount) {
        assertSufficientAmountAndCompanyBalance(company, amount);
        return new GiftDeposit(-1L, amount, company, employee, creationDate(), expirationDate());
    }

    @Override
    public LocalDate expirationDate() {
        return LocalDate.now().plusDays(365);
    }
}
