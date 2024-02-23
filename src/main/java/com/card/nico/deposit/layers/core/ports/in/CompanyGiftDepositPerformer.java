package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.exceptions.NotAnEmployeeException;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class CompanyGiftDepositPerformer implements CompanyDepositPerformer {

    private final Company company;

    public CompanyGiftDepositPerformer(Company company) {
        this.company = requireNonNull(company);
    }

    public CompanyToEmployeeDepositPerformer to(Employee employee) {
        requireNonNull(employee);
        String employeeName = employee.name();
        if(company.employees().stream().noneMatch(candidate -> Objects.equals(candidate.name(), employeeName))) {
            throw new NotAnEmployeeException(company.name(), employeeName);
        }
        return new CompanyToEmployeeDepositPerformer(new GiftDepositStrategy(company, employee));
    }
}
