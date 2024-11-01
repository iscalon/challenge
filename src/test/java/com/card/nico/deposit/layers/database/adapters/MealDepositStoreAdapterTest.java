package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.*;
import com.card.nico.deposit.layers.core.ports.in.DepositUseCase;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;
import com.card.nico.deposit.layers.database.tooling.DBTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DBTest
class MealDepositStoreAdapterTest {

    private static final String EURO_CODE = "EUR";

    @Inject
    private DepositUseCase deposits;

    @Inject
    private EmployeeStore employeeStore;

    @Inject
    private CompanyStore companyStore;

    @Test
    @DisplayName("Can store a core meal deposit")
    @Transactional
    void test01() {
        MoneyAmount giftAmount = MoneyAmount.of(230, EURO_CODE);
        Deposit mealDeposit = createCoreMealDeposit(giftAmount);
        deposits.type("MEAL").save(mealDeposit);

        List<MealDeposit> mealDeposits = deposits.type("MEAL").findAll();
        assertThat(mealDeposits)
                .extracting(MealDeposit::getAmount)
                .containsExactly(giftAmount);
    }

    private Deposit createCoreMealDeposit(MoneyAmount amount) {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companyStore.save(company);

        return deposits.type("MEAL")
                .from(company.name())
                .to(john.name())
                .doDeposit(amount);
    }
}