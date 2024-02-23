package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;

import static java.util.Objects.requireNonNull;

public class CompanyGiftDepositPerformer implements CompanyDepositPerformer {

    private final Company company;

    public CompanyGiftDepositPerformer(Company company) {
        this.company = requireNonNull(company);
    }

    public CompanyToEmployeeDepositPerformer to(Employee employee) {
        requireNonNull(employee);
        return new CompanyToEmployeeDepositPerformer(new GiftDepositStrategy(company, employee));
    }
}
