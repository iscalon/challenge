package com.example.demo.business;

import com.example.demo.data.Company;
import com.example.demo.data.Deposit;
import com.example.demo.data.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

/**
 * Allow to perform deposits using a strategy pattern.
 */
public class Deposits {

    private final DepositStrategy strategy;
    private final Company company;
    private final User user;

    private Deposits(DepositStrategy strategy, Company company, User user) {
        this.strategy = strategy;
        this.company = company;
        this.user = user;
    }

    public Deposit doDeposit(BigDecimal amount) {
        if(amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than or equals to 0");
        }
        return strategy.execute(company, user, amount);
    }

    @Component
    public static class Factory {

        /** All available strategies */
        private final Map<String, DepositStrategy> depositStrategies;

        private Factory(Collection<DepositStrategy> depositStrategies) {
            requireNonNull(depositStrategies);
            this.depositStrategies = depositStrategies.stream().collect(toMap(DepositStrategy::getTypeName, Function.identity()));
        }

        public Deposits create(String strategyTypeName, Company company, User user) {
            DepositStrategy strategy = this.depositStrategies.get(strategyTypeName);
            if(strategy == null) {
                throw new NoSuchElementException("Deposit strategy type name : " + strategyTypeName + " not found");
            }
            return new Deposits(strategy, requireNonNull(company), requireNonNull(user));
        }
    }
}
