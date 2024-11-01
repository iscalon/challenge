package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.*;
import com.card.nico.deposit.layers.core.annotations.DomainComponent;
import com.card.nico.deposit.layers.core.exceptions.CompanyNotFoundException;
import com.card.nico.deposit.layers.core.exceptions.EmployeeNotFoundException;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;
import com.card.nico.deposit.layers.core.ports.out.GiftDepositStore;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@DomainComponent
class GiftDepositStrategy implements DepositStrategy {

    private final CompanyStore companyStore;
    private final EmployeeStore employeeStore;
    private final GiftDepositStore depositStore;

    public GiftDepositStrategy(CompanyStore companyStore, EmployeeStore employeeStore, GiftDepositStore depositStore) {
        this.companyStore = requireNonNull(companyStore);
        this.employeeStore = requireNonNull(employeeStore);
        this.depositStore = requireNonNull(depositStore);
    }

    @Override
    public Deposit createDeposit(String companyName, String employeeName, MoneyAmount amount) {
        Company company = companyStore
                .findByName(companyName)
                .orElseThrow(() -> new CompanyNotFoundException(companyName));
        Employee employee = employeeStore
                .findByName(employeeName)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeName));
        DepositValidator
                .company(company)
                .depositAmount(amount)
                .assertAmountAndCompanyBalanceAreSufficient();
        return depositStore.save(new GiftDeposit(-1L, amount, company, employee, creationDate(), expirationDate()));
    }

    @Override
    public LocalDate expirationDate() {
        return LocalDate.now().plusDays(365);
    }

    @Override
    public <T extends Deposit> Optional<T> findById(Long id) {
        return depositStore.findById(id);
    }

    @Override
    public <T extends Deposit> List<T> findByEmployeeName(String employeeName) {
        return depositStore.findByEmployeeName(employeeName);
    }

    @Override
    public <T extends Deposit> List<T> findAll() {
        return depositStore.findAll();
    }

    @Override
    public String depositType() {
        return "GIFT";
    }
}
