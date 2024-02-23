package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

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
        assertSufficientAmountAndCompanyBalance(company, amount);
        return new MealDeposit(-1L, amount, company, employee, creationDate(), expirationDate());
    }

    @Override
    public LocalDate expirationDate() {
        int nextYear = LocalDate.now().getYear() + 1;
        return LocalDate.of(nextYear, Month.FEBRUARY, 1).with(TemporalAdjusters.lastDayOfMonth());
    }
}
