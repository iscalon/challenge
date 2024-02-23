package com.card.nico.deposit.layers.core.ports.out;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;

import java.util.Collection;
import java.util.Optional;

public interface EmployeeStore {

    void save(Employee employee);

    Optional<Employee> findByName(String name);

    Collection<Employee> findAll();

    Optional<Company> findCompany(String employeeName);
}
