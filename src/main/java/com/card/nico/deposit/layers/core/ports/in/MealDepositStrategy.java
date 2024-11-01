package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.*;
import com.card.nico.deposit.layers.core.annotations.DomainComponent;
import com.card.nico.deposit.layers.core.exceptions.CompanyNotFoundException;
import com.card.nico.deposit.layers.core.exceptions.EmployeeNotFoundException;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;
import com.card.nico.deposit.layers.core.ports.out.DepositStore;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@DomainComponent
class MealDepositStrategy implements DepositStrategy {

    private final CompanyStore companyStore;
    private final EmployeeStore employeeStore;
    private final DepositStore depositStore;

    public MealDepositStrategy(CompanyStore companyStore, EmployeeStore employeeStore, List<DepositStore> depositStores) {
        this.companyStore = requireNonNull(companyStore);
        this.employeeStore = requireNonNull(employeeStore);
        this.depositStore = Optional.ofNullable(depositStores)
                .orElseGet(List::of)
                .stream()
                .filter(store -> Objects.equals(store.type(), depositType()))
                .findAny()
                .orElseThrow();
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
        return depositStore.save(new MealDeposit(-1L, amount, company, employee, creationDate(), expirationDate()));
    }

    @Override
    public LocalDate expirationDate() {
        int nextYear = LocalDate.now().getYear() + 1;
        return LocalDate.of(nextYear, Month.FEBRUARY, 1).with(TemporalAdjusters.lastDayOfMonth());
    }

    @Override
    public <T extends Deposit> Optional<T> findById(Long id) {
        return depositStore.findById(id);
    }

    @Override
    public <T extends Deposit> List<T> findAll() {
        return depositStore.findAll();
    }

    @Override
    public Deposit save(Deposit deposit) {
        return depositStore.save(deposit);
    }

    @Override
    public <T extends Deposit> List<T> findByEmployeeName(String employeeName) {
        return depositStore.findByEmployeeName(employeeName);
    }

    @Override
    public String depositType() {
        return "MEAL";
    }
}
