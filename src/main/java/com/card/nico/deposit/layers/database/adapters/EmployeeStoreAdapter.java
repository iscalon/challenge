package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;
import com.card.nico.deposit.layers.database.EmployeeEntity;
import com.card.nico.deposit.layers.database.EmployeeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
class EmployeeStoreAdapter implements EmployeeStore {

    private final EmployeeRepository employeeRepository;
    private final EmployeeEntityConverter employeeEntityConverter;
    private final CompanyEntityConverter companyEntityConverter;

    EmployeeStoreAdapter(EmployeeRepository employeeRepository, EmployeeEntityConverter employeeEntityConverter, CompanyEntityConverter companyEntityConverter) {
        this.employeeRepository = requireNonNull(employeeRepository);
        this.employeeEntityConverter = requireNonNull(employeeEntityConverter);
        this.companyEntityConverter = requireNonNull(companyEntityConverter);
    }

    @Transactional
    @Override
    public void save(Employee employee) {
        employeeRepository.save(employeeEntityConverter.fromCoreEmployee(employee));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Employee> findByName(String name) {
        return employeeRepository.findByName(name)
                .map(employeeEntityConverter::toCoreEmployee);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Employee> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeEntityConverter::toCoreEmployee)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Company> findCompany(String employeeName) {
        return employeeRepository
                .findByName(employeeName)
                .map(EmployeeEntity::getId)
                .flatMap(employeeRepository::findCompanyByEmployeeId)
                .map(companyEntityConverter::toCoreCompany);
    }
}
