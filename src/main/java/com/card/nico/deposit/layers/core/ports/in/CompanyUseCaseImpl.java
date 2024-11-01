package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.annotations.UseCase;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;
import com.card.nico.deposit.layers.core.ports.out.TransactionPort;

import java.util.Collection;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@UseCase
class CompanyUseCaseImpl implements CompanyUseCase {

    private final CompanyStore companies;
    private final EmployeeStore employees;
    private final TransactionPort transactionPort;

    CompanyUseCaseImpl(CompanyStore companies, EmployeeStore employees, TransactionPort transactionPort) {
        this.companies = requireNonNull(companies);
        this.employees = requireNonNull(employees);
        this.transactionPort = requireNonNull(transactionPort);
    }

    @Override
    public void save(Company company) {
        transactionPort.executeTransactionally(() -> {
            companies.save(company);
            return null;
        });
    }

    @Override
    public Optional<Company> findByName(String name) {
        return transactionPort.executeTransactionally(() ->
                companies.findByName(name));
    }

    @Override
    public Optional<Company> findByEmployeeName(String name) {
        return transactionPort.executeTransactionally(() ->
                employees.findCompany(name));
    }

    @Override
    public Collection<Company> findAll() {
        return transactionPort.executeTransactionally(companies::findAll);
    }
}
