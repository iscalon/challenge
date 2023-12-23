package com.example.demo.business;

import com.example.demo.data.Company;
import com.example.demo.data.CompanyFixture;
import com.example.demo.data.Deposit;
import com.example.demo.data.User;
import com.example.demo.data.UserFixture;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DepositsTest {

    @Inject
    private Deposits.Factory deposits;
    @Inject
    private CompanyFixture companyFixture;
    @Inject
    private UserFixture userFixture;

    @Test
    @DisplayName("""
            GIVEN a company which balance is 888 and an user
            WHEN a 777 amount gift deposit is done by the company to the user
            THEN the deposit expiration date is 365 days later from now and the company balance is now 111
            """)
    void test01() {
        Company company = companyFixture.create("Total", BigDecimal.valueOf(888d));
        User user = userFixture.create("John");
        Deposits giftDeposits = this.deposits.create("gift", company, user);

        Deposit deposit = giftDeposits.doDeposit(BigDecimal.valueOf(777d));
        assertThat(deposit.expirationDate())
                .isEqualTo(LocalDate.now().plusDays(365));
        assertThat(company.getBalance())
                .hasValue(BigDecimal.valueOf(111d));
    }

    @Test
    @DisplayName("""
            GIVEN a company which balance is 888 and an user
            WHEN a 999 amount gift deposit is done by the company to the user
            THEN an IllegalArgumentException should be raised with message : "Gift amount : 999.0 is greater than the company balance"
            """)
    void test02() {
        Company company = companyFixture.create("Total", BigDecimal.valueOf(888d));
        User user = userFixture.create("John");
        Deposits giftDeposits = this.deposits.create("gift", company, user);

        BigDecimal amount = BigDecimal.valueOf(999d);
        assertThatThrownBy(() -> giftDeposits.doDeposit(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gift amount : 999.0 is greater than the company balance");
    }

    @Test
    @DisplayName("""
            GIVEN a company and an user
            WHEN a null amount gift deposit is done by the company to the user
            THEN an IllegalArgumentException should be raised with message : "Amount cannot be null"
            """)
    void test03() {
        Company company = companyFixture.create("Total");
        User user = userFixture.create("John");
        Deposits giftDeposits = this.deposits.create("gift", company, user);

        assertThatThrownBy(() -> giftDeposits.doDeposit(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount cannot be null");
    }

    @Test
    @DisplayName("""
            GIVEN a company and an user
            WHEN a negative amount gift deposit is done by the company to the user
            THEN an IllegalArgumentException should be raised with message : "Amount must be greater than or equals to 0"
            """)
    void test04() {
        Company company = companyFixture.create("Total");
        User user = userFixture.create("John");
        Deposits giftDeposits = this.deposits.create("gift", company, user);

        BigDecimal amount = BigDecimal.valueOf(-1d);
        assertThatThrownBy(() -> giftDeposits.doDeposit(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than or equals to 0");
    }

    @Test
    @DisplayName("""
            GIVEN a company and an user
            WHEN a "unknown" deposit type is done by the company to the user
            THEN a NoSuchElementException should be raised with message : "Deposit strategy type name : unknown not found"
            """)
    void test05() {
        Company company = companyFixture.create("Total");
        User user = userFixture.create("John");

        assertThatThrownBy(() -> this.deposits.create("unknown", company, user))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Deposit strategy type name : unknown not found");
    }

    @Test
    @DisplayName("""
            GIVEN a company which balance is 888 and an user
            WHEN a 777 amount meal deposit is done by the company to the user
            THEN the deposit expiration date is end of february one year later from now and the company balance is now 111
            """)
    void test06() {
        Company company = companyFixture.create("Total", BigDecimal.valueOf(888d));
        User user = userFixture.create("John");
        Deposits mealDeposits = this.deposits.create("meal", company, user);

        int nextYear = LocalDate.now().getYear() + 1;
        Deposit deposit = mealDeposits.doDeposit(BigDecimal.valueOf(777d));
        assertThat(deposit.expirationDate())
                .isEqualTo(LocalDate.of(nextYear, Month.FEBRUARY, 1).with(TemporalAdjusters.lastDayOfMonth()));
        assertThat(company.getBalance())
                .hasValue(BigDecimal.valueOf(111d));
    }

    @Test
    @DisplayName("""
            GIVEN a company which balance is 888 and an user
            WHEN a 999 amount meal deposit is done by the company to the user
            THEN an IllegalArgumentException should be raised with message : "Meal amount : 999.0 is greater than the company balance"
            """)
    void test07() {
        Company company = companyFixture.create("Total", BigDecimal.valueOf(888d));
        User user = userFixture.create("John");
        Deposits mealDeposits = this.deposits.create("meal", company, user);

        BigDecimal amount = BigDecimal.valueOf(999d);
        assertThatThrownBy(() -> mealDeposits.doDeposit(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Meal amount : 999.0 is greater than the company balance");
    }

    @Test
    @DisplayName("""
            GIVEN a company and an user
            WHEN a null amount meal deposit is done by the company to the user
            THEN an IllegalArgumentException should be raised with message : "Amount cannot be null"
            """)
    void test08() {
        Company company = companyFixture.create("Total");
        User user = userFixture.create("John");
        Deposits mealDeposits = this.deposits.create("meal", company, user);

        assertThatThrownBy(() -> mealDeposits.doDeposit(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount cannot be null");
    }

    @Test
    @DisplayName("""
            GIVEN a company and an user
            WHEN a negative amount meal deposit is done by the company to the user
            THEN an IllegalArgumentException should be raised with message : "Amount must be greater than or equals to 0"
            """)
    void test09() {
        Company company = companyFixture.create("Total");
        User user = userFixture.create("John");
        Deposits mealDeposits = this.deposits.create("meal", company, user);

        BigDecimal amount = BigDecimal.valueOf(-1d);
        assertThatThrownBy(() -> mealDeposits.doDeposit(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than or equals to 0");
    }
}