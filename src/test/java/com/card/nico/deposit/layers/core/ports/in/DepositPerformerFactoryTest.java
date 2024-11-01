package com.card.nico.deposit.layers.core.ports.in;


import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.exceptions.DepositCoreException;
import com.card.nico.deposit.layers.core.exceptions.InsufficientCompanyBalanceException;
import com.card.nico.deposit.layers.ports.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepositPerformerFactoryTest {

    private static final String EURO_CODE = "EUR";

    private final FakeCompanyStore companyStore = new FakeCompanyStore();
    private final FakeEmployeeStore employeeStore = new FakeEmployeeStore();
    private final FakeGiftDepositStore giftDepositStore = new FakeGiftDepositStore();
    private final FakeMealDepositStore mealDepositStore = new FakeMealDepositStore();
    private final List<DepositStrategy> depositStrategies = List.of(
            new GiftDepositStrategy(companyStore, employeeStore, List.of(giftDepositStore)),
            new MealDepositStrategy(companyStore, employeeStore, List.of(mealDepositStore))
    );

    @BeforeEach
    void setUp() {
        companyStore.clear();
        employeeStore.clear();
        giftDepositStore.clear();
        mealDepositStore.clear();
        employeeStore.setCompany(null);
    }

    @Test
    @DisplayName("A company can create a gift deposit to an employee")
    void test01() {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        MoneyAmount giftAmount = MoneyAmount.of(500d, EURO_CODE);
        DepositUseCase depositUseCase = new DepositPerformerFactory(depositStrategies, companyStore,
                new FakeTransactionalPort());
        Deposit deposit = depositUseCase
                .type("GIFT")
                .from(company.name())
                .to(john.name())
                .doDeposit(giftAmount);

        assertThat(deposit.getEmployee())
                .isEqualTo(john);
        assertThat(deposit.getCompany())
                .isEqualTo(company);
        assertThat(deposit.getCreationDate())
                .isNotNull();
        assertThat(deposit.getAmount())
                .isEqualTo(giftAmount);
        assertThat(deposit.getType())
                .isEqualTo("GIFT");
        LocalDate depositCreationDate = deposit.getCreationDate().toLocalDate();
        assertThat(deposit.getExpirationDate())
                .hasValue(depositCreationDate.plusDays(365));
    }

    @Test
    @DisplayName("A company can create a meal deposit to an employee")
    void test02() {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        MoneyAmount giftAmount = MoneyAmount.of(500d, EURO_CODE);
        DepositUseCase depositUseCase = new DepositPerformerFactory(depositStrategies, companyStore,
                new FakeTransactionalPort());
        Deposit deposit = depositUseCase
                .type("MEAL")
                .from(company.name())
                .to(john.name())
                .doDeposit(giftAmount);

        assertThat(deposit.getEmployee())
                .isEqualTo(john);
        assertThat(deposit.getCompany())
                .isEqualTo(company);
        assertThat(deposit.getCreationDate())
                .isNotNull();
        assertThat(deposit.getAmount())
                .isEqualTo(giftAmount);
        assertThat(deposit.getType())
                .isEqualTo("MEAL");
        LocalDate depositCreationDate = deposit.getCreationDate().toLocalDate();
        assertThat(deposit.getExpirationDate())
                .hasValue(mealExpirationDate(depositCreationDate));
    }

    @Test
    @DisplayName("A company cannot create a gift deposit to an employee if company's balance is insufficient")
    void test03() {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(90d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        MoneyAmount giftAmount = MoneyAmount.of(500d, EURO_CODE);
        DepositUseCase depositUseCase = new DepositPerformerFactory(depositStrategies, companyStore,
                new FakeTransactionalPort());
        CompanyToEmployeeDepositKindPerformer toEmployeeDepositPerformer = depositUseCase
                .type("GIFT")
                .from(company.name())
                .to(john.name());

        assertThatThrownBy(() -> toEmployeeDepositPerformer
                .doDeposit(giftAmount))
                .isInstanceOf(InsufficientCompanyBalanceException.class)
                .hasMessage("Total company's balance : 90.00 EUR is insufficient to perform a 500.00 EUR debit");
    }

    @Test
    @DisplayName("A company cannot create a meal deposit to an employee if company's balance is insufficient")
    void test04() {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount mercedesBalance = MoneyAmount.of(90d, EURO_CODE);
        Company company = new Company("Mercedes", mercedesBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        MoneyAmount mealAmount = MoneyAmount.of(500d, EURO_CODE);
        DepositUseCase depositUseCase = new DepositPerformerFactory(depositStrategies, companyStore,
                new FakeTransactionalPort());
        CompanyToEmployeeDepositKindPerformer toEmployeeDepositPerformer = depositUseCase
                .type("MEAL")
                .from(company.name())
                .to(john.name());

        assertThatThrownBy(() -> toEmployeeDepositPerformer
                .doDeposit(mealAmount))
                .isInstanceOf(InsufficientCompanyBalanceException.class)
                .hasMessage("Mercedes company's balance : 90.00 EUR is insufficient to perform a 500.00 EUR debit");
    }


    @Test
    @DisplayName("A company cannot create a gift deposit to an employee if deposit amount is <= 0")
    void test05() {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(90d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        MoneyAmount giftAmount = MoneyAmount.of(0d, EURO_CODE);
        DepositUseCase depositUseCase = new DepositPerformerFactory(depositStrategies, companyStore,
                new FakeTransactionalPort());
        CompanyToEmployeeDepositKindPerformer toEmployeeDepositPerformer = depositUseCase.type("GIFT")
                .from(company.name())
                .to(john.name());
        assertThatThrownBy(() -> toEmployeeDepositPerformer
                .doDeposit(giftAmount))
                .isInstanceOf(DepositCoreException.class)
                .hasMessage("Amount must be greater than 0");
    }

    @Test
    @DisplayName("A company cannot create a meal deposit to an employee if deposit amount is <= 0")
    void test06() {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount mercedesBalance = MoneyAmount.of(90d, EURO_CODE);
        Company company = new Company("Mercedes", mercedesBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        MoneyAmount mealAmount = MoneyAmount.of(0d, EURO_CODE);
        DepositUseCase depositUseCase = new DepositPerformerFactory(depositStrategies, companyStore,
                new FakeTransactionalPort());
        CompanyToEmployeeDepositKindPerformer toEmployeeDepositPerformer = depositUseCase.type("MEAL")
                .from(company.name())
                .to(john.name());

        assertThatThrownBy(() -> toEmployeeDepositPerformer
                .doDeposit(mealAmount))
                .isInstanceOf(DepositCoreException.class)
                .hasMessage("Amount must be greater than 0");
    }

    private static LocalDate mealExpirationDate(LocalDate depositCreationDate) {
        int nextYear = depositCreationDate.getYear() + 1;
        return LocalDate.of(nextYear, Month.FEBRUARY, 1).with(TemporalAdjusters.lastDayOfMonth());
    }
}