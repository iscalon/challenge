package com.card.nico.deposit.layers.database;

import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.database.tooling.DBTest;
import jakarta.inject.Inject;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DBTest
class CompanyRepositoryTest {

    private static final Currency EUROS = Currency.getInstance(Locale.FRANCE);

    @Inject
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("No existing company on init")
    void test01() {
        List<CompanyEntity> allCompanies = companyRepository.findAll();

        assertThat(allCompanies)
                .isEmpty();
    }

    @Test
    @DisplayName("Company creation works")
    void test02() {
        companyRepository.save(new CompanyEntity("Alize"));
        List<CompanyEntity> allCompanies = companyRepository.findAll();

        MoneyAmount moneyAmount = new MoneyAmount(BigDecimal.ZERO, EUROS);
        assertThat(allCompanies)
                .isNotEmpty()
                .extracting(CompanyEntity::getName, CompanyEntity::getBalanceAmount)
                .containsExactly(tuple("Alize", moneyAmount));
    }

    @Test
    @DisplayName("Company update works")
    void test03() {
        companyRepository.save(new CompanyEntity("Alize"));
        List<CompanyEntity> allCompanies = companyRepository.findAll();

        assertThat(allCompanies)
                .isNotEmpty()
                .extracting(CompanyEntity::getName, CompanyEntity::getBalanceAmount)
                .containsExactly(tuple("Alize", new MoneyAmount(BigDecimal.ZERO, EUROS)));

        CompanyEntity companyEntity = allCompanies.getFirst();
        MoneyAmount balanceAmount = new MoneyAmount(BigDecimal.valueOf(50000d), EUROS);
        companyEntity.setBalanceAmount(balanceAmount);
        companyRepository.save(companyEntity);

        allCompanies = companyRepository.findAll();

        assertThat(allCompanies)
                .isNotEmpty()
                .extracting(CompanyEntity::getName, CompanyEntity::getBalanceAmount)
                .containsExactly(tuple("Alize", balanceAmount));
    }

    @Test
    @DisplayName("Adding an employee to a company works")
    @Transactional
    void test04() {
        CompanyEntity company = new CompanyEntity("Alize");
        EmployeeEntity employee = new EmployeeEntity("John");

        company.addEmployee(employee);

        Long companyId = companyRepository.save(company).getId();

        company = companyRepository.findById(companyId).orElseThrow();
        Set<EmployeeEntity> companyEmployees = company.getEmployees();
        assertThat(companyEmployees)
                .isNotEmpty()
                .extracting(EmployeeEntity::getName)
                .containsExactly("John");
        assertThat(companyEmployees)
                .extracting(EmployeeEntity::getId)
                .doesNotContainNull();
    }
}