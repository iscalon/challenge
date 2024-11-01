package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Employee;

import java.util.Collection;
import java.util.Optional;

public interface EmployeeUseCase {

    void save(Employee employee);

    Optional<Employee> findByName(String name);

    Collection<Employee> findAll();
}
