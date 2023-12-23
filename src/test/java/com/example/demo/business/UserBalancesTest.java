package com.example.demo.business;

import com.example.demo.data.Company;
import com.example.demo.data.CompanyFixture;
import com.example.demo.data.User;
import com.example.demo.data.UserFixture;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserBalancesTest {

    @Inject
    private UserBalances userBalances;
    @Inject
    private Deposits.Factory deposits;
    @Inject
    private CompanyFixture companyFixture;
    @Inject
    private UserFixture userFixture;

    @Test
    @DisplayName("""
            GIVEN a company with enough balance that do a 28 amount gift deposit, a 11 amount gift deposit and a 9 amount meal deposit to an user
            WHEN requesting balance for this user
            THEN the returned amount is 48
            """)
    void test01() {
        Company company = companyFixture.create("Total", BigDecimal.valueOf(888d));
        User user = userFixture.create("John");
        Deposits giftDeposits = this.deposits.create("gift", company, user);
        Deposits mealDeposits = this.deposits.create("meal", company, user);

        giftDeposits.doDeposit(BigDecimal.valueOf(28d));
        giftDeposits.doDeposit(BigDecimal.valueOf(11d));
        mealDeposits.doDeposit(BigDecimal.valueOf(9d));

        BigDecimal balance = userBalances.computeBalance(user);
        assertThat(balance).isEqualTo(BigDecimal.valueOf(48d));
    }
}