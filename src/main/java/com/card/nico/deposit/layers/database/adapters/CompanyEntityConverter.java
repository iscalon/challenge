package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.database.CompanyEntity;
import com.card.nico.deposit.layers.database.CompanyRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
class CompanyEntityConverter {

    private final CompanyRepository companyRepository;
    private final EmployeeEntityConverter employeeEntityConverter;

    CompanyEntityConverter(CompanyRepository companyRepository, EmployeeEntityConverter employeeEntityConverter) {
        this.companyRepository = requireNonNull(companyRepository);
        this.employeeEntityConverter = requireNonNull(employeeEntityConverter);
    }

    @Transactional
    public CompanyEntity fromCoreCompany(Company company) {
        if(company == null) {
            return  null;
        }

        CompanyEntity companyEntity = this.companyRepository.findByName(company.name())
                .map(entity -> updateCompanyEntity(entity, company))
                .orElseGet(() -> createCompanyEntity(company));

        return companyRepository.save(companyEntity);
    }

    private CompanyEntity updateCompanyEntity(CompanyEntity companyEntity, Company company) {
        company.employees().stream()
                .map(employeeEntityConverter::fromCoreEmployee)
                .distinct()
                .forEach(companyEntity::addEmployee);

        companyEntity.setBalanceAmount(company.balance());

        return companyEntity;
    }

    private CompanyEntity createCompanyEntity(Company company) {
        CompanyEntity companyEntity = new CompanyEntity(company.name(), company.balance());
        return updateCompanyEntity(companyEntity, company);
    }

    @Transactional
    public Company toCoreCompany(CompanyEntity companyEntity) {
        if(companyEntity == null) {
            return null;
        }
        String companyName = companyEntity.getName();
        MoneyAmount companyBalance = companyEntity.getBalanceAmount();
        Set<Employee> employees = companyEntity.getEmployees().stream().map(employeeEntityConverter::toCoreEmployee).collect(Collectors.toSet());
        return new Company(companyName, companyBalance, employees);
    }
}
