package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.database.EmployeeEntity;
import com.card.nico.deposit.layers.database.EmployeeRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class EmployeeEntityConverter {

    private final EmployeeRepository employeeRepository;

    EmployeeEntityConverter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    EmployeeEntity fromCoreEmployee(Employee employee) {
        if(employee == null) {
            return  null;
        }
        Optional<EmployeeEntity> employeeEntity = this.employeeRepository.findByName(employee.name());
        return employeeEntity
                .orElseGet(() -> createAndSaveEmployee(employee));
    }

    private EmployeeEntity createAndSaveEmployee(Employee employee) {
        EmployeeEntity employeeEntity = new EmployeeEntity(employee.name());
        return employeeRepository.save(employeeEntity);
    }

    Employee toCoreEmployee(EmployeeEntity employeeEntity) {
        if(employeeEntity == null) {
            return null;
        }
        return new Employee(employeeEntity.getName());
    }
}
