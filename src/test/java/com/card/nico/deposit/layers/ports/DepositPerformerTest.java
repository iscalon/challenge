package com.card.nico.deposit.layers.ports;


import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.exceptions.InsufficientCompanyBalanceException;
import com.card.nico.deposit.layers.core.ports.in.CompanyDepositPerformer;
import com.card.nico.deposit.layers.core.ports.in.CompanyGiftDepositPerformer;
import com.card.nico.deposit.layers.core.ports.in.CompanyMealDepositPerformer;
import com.card.nico.deposit.layers.core.ports.in.CompanyToEmployeeDepositPerformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepositPerformerTest {

    private static final String EURO_CODE = "EUR";

    @Test
    @DisplayName("A company can create a gift deposit to an employee")
    void test01() {
        Employee john = new Employee("John");
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company total = new Company("Total", totalBalance, Set.of(john));
        MoneyAmount giftAmount = MoneyAmount.of(500d, EURO_CODE);
        CompanyDepositPerformer companyDepositPerformer = new CompanyGiftDepositPerformer(total);
        Deposit deposit = companyDepositPerformer
                .to(john)
                .doDeposit(giftAmount);

        assertThat(deposit.getEmployee())
                .isEqualTo(john);
        assertThat(deposit.getCompany())
                .isEqualTo(total);
        assertThat(deposit.getCreationDate())
                .isNotNull();
        assertThat(deposit.getAmount())
                .isEqualTo(giftAmount);
        assertThat(deposit.getType())
                .isEqualTo("gift");
        LocalDate depositCreationDate = deposit.getCreationDate().toLocalDate();
        assertThat(deposit.getExpirationDate())
                .hasValue(depositCreationDate.plusDays(365));
    }

    @Test
    @DisplayName("A company can create a meal deposit to an employee")
    void test02() {
        Employee john = new Employee("John");
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company total = new Company("Total", totalBalance, Set.of(john));
        MoneyAmount giftAmount = MoneyAmount.of(500d, EURO_CODE);
        CompanyDepositPerformer companyDepositPerformer = new CompanyMealDepositPerformer(total);
        Deposit deposit = companyDepositPerformer
                .to(john)
                .doDeposit(giftAmount);

        assertThat(deposit.getEmployee())
                .isEqualTo(john);
        assertThat(deposit.getCompany())
                .isEqualTo(total);
        assertThat(deposit.getCreationDate())
                .isNotNull();
        assertThat(deposit.getAmount())
                .isEqualTo(giftAmount);
        assertThat(deposit.getType())
                .isEqualTo("meal");
        LocalDate depositCreationDate = deposit.getCreationDate().toLocalDate();
        assertThat(deposit.getExpirationDate())
                .hasValue(depositCreationDate.plusMonths(2));
    }

    @Test
    @DisplayName("A company cannot create a gift deposit to an employee if company's balance is insufficient")
    void test03() {
        Employee john = new Employee("John");
        MoneyAmount totalBalance = MoneyAmount.of(90d, EURO_CODE);
        Company total = new Company("Total", totalBalance, Set.of(john));
        MoneyAmount giftAmount = MoneyAmount.of(500d, EURO_CODE);
        CompanyDepositPerformer companyDepositPerformer = new CompanyGiftDepositPerformer(total);
        CompanyToEmployeeDepositPerformer companyToEmployeeDepositPerformer = companyDepositPerformer
                .to(john);

        assertThatThrownBy(() -> companyToEmployeeDepositPerformer
                .doDeposit(giftAmount))
                .isInstanceOf(InsufficientCompanyBalanceException.class)
                .hasMessage("Total company's balance : 90.00 EUR is insufficient to perform a 500.00 EUR debit");
    }

    @Test
    @DisplayName("A company cannot create a meal deposit to an employee if company's balance is insufficient")
    void test04() {
        Employee john = new Employee("John");
        MoneyAmount mercedesBalance = MoneyAmount.of(90d, EURO_CODE);
        Company total = new Company("Mercedes", mercedesBalance, Set.of(john));
        MoneyAmount mealAmount = MoneyAmount.of(500d, EURO_CODE);
        CompanyDepositPerformer companyDepositPerformer = new CompanyMealDepositPerformer(total);
        CompanyToEmployeeDepositPerformer companyToEmployeeDepositPerformer = companyDepositPerformer
                .to(john);

        assertThatThrownBy(() -> companyToEmployeeDepositPerformer
                .doDeposit(mealAmount))
                .isInstanceOf(InsufficientCompanyBalanceException.class)
                .hasMessage("Mercedes company's balance : 90.00 EUR is insufficient to perform a 500.00 EUR debit");
    }
}