package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.exceptions.DepositCoreException;
import com.card.nico.deposit.layers.core.exceptions.InsufficientCompanyBalanceException;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

class DepositValidator {

    private final Company company;

    private DepositValidator(Company company) {
        this.company = requireNonNull(company);
    }

    static DepositValidator company(Company company) {
        return new DepositValidator(company);
    }

    CompanyDepositValidator depositAmount(MoneyAmount amount) {
        return new CompanyDepositValidator(amount);
    }

    class CompanyDepositValidator {

        private final MoneyAmount amount;

        private CompanyDepositValidator(MoneyAmount amount) {
            this.amount = requireNonNull(amount);
        }

        void assertAmountAndCompanyBalanceAreSufficient() {
            BigDecimal debit = amount.amount();
            if (debit.compareTo(BigDecimal.ZERO) <= 0) {
                throw new DepositCoreException("Amount must be greater than 0");
            }
            BigDecimal companyBalance = company.balance().amount();
            if (companyBalance.compareTo(debit) < 0) {
                throw new InsufficientCompanyBalanceException(company, amount);
            }
        }
    }
}
