package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.*;
import com.card.nico.deposit.layers.database.EmployeeEntity;
import com.card.nico.deposit.layers.database.EmployeeRepository;
import com.card.nico.deposit.layers.database.MealDepositEntity;
import com.card.nico.deposit.layers.database.MealDepositRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
class MealDepositEntityConverter {

    private final MealDepositRepository mealDepositRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeEntityConverter employeeEntityConverter;
    private final CompanyEntityConverter companyEntityConverter;

    MealDepositEntityConverter(MealDepositRepository mealDepositRepository, EmployeeRepository employeeRepository, EmployeeEntityConverter employeeEntityConverter, CompanyEntityConverter companyEntityConverter) {
        this.mealDepositRepository = requireNonNull(mealDepositRepository);
        this.employeeRepository = requireNonNull(employeeRepository);
        this.employeeEntityConverter = requireNonNull(employeeEntityConverter);
        this.companyEntityConverter = requireNonNull(companyEntityConverter);
    }

    @Transactional
    public MealDepositEntity fromCoreMealDeposit(Deposit mealDeposit) {
        if(mealDeposit == null) {
            return null;
        }

        Optional<MealDepositEntity> mealDepositEntity = this.mealDepositRepository.findByCreationDate(mealDeposit.getCreationDate());
        return mealDepositEntity
                .orElseGet(() -> createAndSaveMealDeposit(mealDeposit));
    }

    private MealDepositEntity createAndSaveMealDeposit(Deposit mealDeposit) {
        MoneyAmount amount = mealDeposit.getAmount();
        EmployeeEntity employee = employeeEntityConverter.fromCoreEmployee(mealDeposit.getEmployee());
        MealDepositEntity mealDepositEntity = new MealDepositEntity(amount, employee);
        createCompanyEntityIfNeeded(mealDeposit.getCompany());
        mealDeposit.getExpirationDate()
                .ifPresent(mealDepositEntity::setExpirationDate);
        return mealDepositRepository.save(mealDepositEntity);
    }

    private void createCompanyEntityIfNeeded(Company company) {
        companyEntityConverter.fromCoreCompany(company);
    }

    @Transactional
    public MealDeposit toCoreMealDeposit(MealDepositEntity mealDepositEntity) {
        if(mealDepositEntity == null) {
            return null;
        }
        MoneyAmount amount = mealDepositEntity.getMoneyAmount();
        EmployeeEntity employeeEntity = mealDepositEntity.getEmployee();
        Employee employee = employeeEntityConverter.toCoreEmployee(employeeEntity);
        Company company = employeeRepository.findCompanyByEmployeeId(employeeEntity.getId())
                .map(companyEntityConverter::toCoreCompany)
                .orElseThrow();
        LocalDateTime creationDate = mealDepositEntity.getCreationDate();
        LocalDate expirationDate = mealDepositEntity.getExpirationDate().orElse(null);
        return new MealDeposit(mealDepositEntity.getId(), amount,company,employee, creationDate,expirationDate);
    }
}
