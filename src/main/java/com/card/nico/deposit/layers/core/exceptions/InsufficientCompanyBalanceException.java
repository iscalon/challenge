package com.card.nico.deposit.layers.core.exceptions;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.MoneyAmount;

import java.util.Optional;

public class InsufficientCompanyBalanceException extends DepositCoreException {

    public InsufficientCompanyBalanceException(Company company, MoneyAmount debit) {
        super(message(company, debit));
    }

    private static String message(Company company, MoneyAmount debit) {
        String companyName = Optional.ofNullable(company)
                .map(Company::name)
                .orElse(null);
        String balance = Optional.ofNullable(company)
                .map(Company::balance)
                .map(moneyAmount -> moneyAmount.amount() + " " + moneyAmount.currency().getCurrencyCode())
                .orElse(null);
        String amount = Optional.ofNullable(debit)
                .map(moneyAmount -> moneyAmount.amount() + " " + moneyAmount.currency().getCurrencyCode())
                .orElse(null);

        return companyName + " company's balance : " + balance + " is insufficient to perform a " + amount + " debit";
    }
}
