package com.card.nico.deposit.layers.database;

import com.card.nico.deposit.layers.database.tooling.DBTest;
import jakarta.inject.Inject;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DBTest
class EmployeeRepositoryTest {

    @Inject
    private EmployeeRepository employeeRepository;
    @Inject
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("no existing employee on init")
    void test01() {
        List<EmployeeEntity> allEmployees = employeeRepository.findAll();

        assertThat(allEmployees)
                .isEmpty();
    }

    @Test
    @DisplayName("employee creation works")
    void test02() {
        employeeRepository.save(new EmployeeEntity("John"));
        List<EmployeeEntity> allEmployees = employeeRepository.findAll();

        assertThat(allEmployees)
                .isNotEmpty()
                .extracting(EmployeeEntity::getName)
                .containsExactly("John");
    }


    @Test
    @DisplayName("Find employee's company works")
    @Transactional
    void test03() {
        CompanyEntity company = new CompanyEntity("Alize");
        EmployeeEntity employee = new EmployeeEntity("John");
        company.addEmployee(employee);
        Long companyId = companyRepository.save(company).getId();

        employee = companyRepository.findById(companyId)
                .map(CompanyEntity::getEmployees)
                .orElseGet(Set::of)
                .stream()
                .findAny()
                .orElseThrow();

        CompanyEntity employingCompany = employeeRepository.findCompanyByEmployeeId(employee.getId()).orElseThrow();
        assertThat(employingCompany.getName())
                .isEqualTo("Alize");
    }
}