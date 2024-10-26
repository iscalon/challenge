package com.card.nico.deposit.layers.ports;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;

import java.util.*;

public class FakeEmployeeStore implements EmployeeStore {

    private final Collection<Employee> employees;
    private Company company;

    public FakeEmployeeStore() {
        this.employees = new HashSet<>();
    }

    @Override
    public void save(Employee employee) {
        this.employees.add(employee);
    }

    @Override
    public Optional<Employee> findByName(String name) {
        return this.employees.stream()
                .filter(employee -> Objects.equals(employee.name(), name))
                .findAny();
    }

    @Override
    public Collection<Employee> findAll() {
        return List.copyOf(this.employees);
    }

    @Override
    public Optional<Company> findCompany(String employeeName) {
        return Optional.ofNullable(company);
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void clear() {
        this.employees.clear();
    }
}
