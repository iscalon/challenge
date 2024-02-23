package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.*;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public class MealDepositStrategy implements DepositStrategy {

    private final Company company;
    private final Employee employee;

    public MealDepositStrategy(Company company, Employee employee) {
        this.company = requireNonNull(company);
        this.employee = requireNonNull(employee);
    }

    @Override
    public Deposit createDeposit(MoneyAmount amount) {
        assertSufficientCompanyBalance(company, amount);
        return new MealDeposit(-1L, amount, company, employee, creationDate(), expirationDate());
    }

    @Override
    public LocalDate expirationDate() {
        return LocalDate.now().plusMonths(2);
    }
}
