package com.example.demo.business;

import com.example.demo.data.Company;
import com.example.demo.data.Deposit;
import com.example.demo.data.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

public class Deposits {

    private final DepositStrategy depositStrategy;
    private final Company company;
    private final User user;

    public Deposits(DepositStrategy depositStrategy, Company company, User user) {
        this.depositStrategy = requireNonNull(depositStrategy);
        this.company = company;
        this.user = user;
    }

    public Deposit doDeposit(BigDecimal amount) {
        if(amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be >= 0");
        }
        return depositStrategy.execute(company, user, amount);
    }

    @Component
    public static class Factory {

        private final Map<String, DepositStrategy> depositStrategies;

        private Factory(Collection<DepositStrategy> depositStrategies) {
            requireNonNull(depositStrategies);
            this.depositStrategies = depositStrategies.stream().collect(toMap(DepositStrategy::getTypeName, Function.identity()));
        }

        public Deposits create(String strategyTypeName, Company company, User user) {
            return new Deposits(this.depositStrategies.get(strategyTypeName), company, user);
        }
    }
}
