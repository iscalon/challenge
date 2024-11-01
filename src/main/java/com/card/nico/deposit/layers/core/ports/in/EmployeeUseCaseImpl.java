package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.annotations.UseCase;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;
import com.card.nico.deposit.layers.core.ports.out.TransactionPort;

import java.util.Collection;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@UseCase
class EmployeeUseCaseImpl implements EmployeeUseCase {

    private final EmployeeStore employees;
    private final TransactionPort transactionPort;

    EmployeeUseCaseImpl(EmployeeStore employees, TransactionPort transactionPort) {
        this.employees = requireNonNull(employees);
        this.transactionPort = requireNonNull(transactionPort);
    }

    @Override
    public void save(Employee employee) {
        transactionPort.executeTransactionally(() -> {
            employees.save(employee);
            return null;
        });
    }

    @Override
    public Optional<Employee> findByName(String name) {
        return transactionPort.executeTransactionally(() ->
                employees.findByName(name));
    }

    @Override
    public Collection<Employee> findAll() {
        return transactionPort.executeTransactionally(employees::findAll);
    }
}
