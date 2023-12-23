package com.example.demo.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public record MealDeposit(BigDecimal amount, LocalDate expirationDate) implements Deposit {

    public MealDeposit(BigDecimal amount, LocalDate expirationDate) {
        this.amount = requireNonNull(amount);
        this.expirationDate = requireNonNull(expirationDate);
    }
}
