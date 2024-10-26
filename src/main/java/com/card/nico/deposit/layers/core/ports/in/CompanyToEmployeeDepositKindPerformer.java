package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;

public interface CompanyToEmployeeDepositKindPerformer {

    Deposit doDeposit(MoneyAmount amount);
}
