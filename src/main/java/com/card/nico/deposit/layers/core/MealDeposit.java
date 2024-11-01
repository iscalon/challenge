package com.card.nico.deposit.layers.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public record MealDeposit(Long id, MoneyAmount amount, Company company, Employee employee, LocalDateTime creationDate, LocalDate expirationDate) implements Deposit {

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public Company getCompany() {
        return company;
    }

    @Override
    public MoneyAmount getAmount() {
        return amount;
    }

    @Override
    public Optional<LocalDate> getExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    @Override
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public String getType() {
        return "MEAL";
    }
}
