package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;

import static java.util.Objects.requireNonNull;

public class CompanyToEmployeeDepositPerformer {

    private final DepositStrategy depositStrategy;

    CompanyToEmployeeDepositPerformer(DepositStrategy depositStrategy) {
        this.depositStrategy = requireNonNull(depositStrategy);
    }

    public Deposit doDeposit(MoneyAmount amount) {
        return depositStrategy.createDeposit(amount);
    }
}
