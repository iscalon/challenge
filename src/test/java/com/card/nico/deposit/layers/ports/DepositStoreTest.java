package com.card.nico.deposit.layers.ports;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.ports.in.CompanyDepositPerformer;
import com.card.nico.deposit.layers.core.ports.in.CompanyGiftDepositPerformer;
import com.card.nico.deposit.layers.core.ports.in.CompanyMealDepositPerformer;
import com.card.nico.deposit.layers.core.ports.out.DepositStore;
import com.card.nico.deposit.layers.core.ports.out.MealDepositStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class DepositStoreTest {

    public static final String EURO_CODE = "EUR";

    @Test
    @DisplayName("Can store a gift deposit")
    void test01() {
        MoneyAmount giftAmount = MoneyAmount.of(660, EURO_CODE);
        Deposit giftDeposit = createGiftDeposit(giftAmount);

        DepositStore depositStore = new FakeGiftDepositStore();
        depositStore.save(giftDeposit);

        List<Deposit> giftDeposits = depositStore.findAll();
        assertThat(giftDeposits)
                .isNotEmpty()
                .extracting(Deposit::getAmount, Deposit::getType)
                .containsExactly(tuple(giftAmount, "gift"));
    }

    @Test
    @DisplayName("Can store a meal deposit")
    void test02() {
        MoneyAmount mealAmount = MoneyAmount.of(660, EURO_CODE);
        Deposit mealDeposit = createMealDeposit(mealAmount);

        MealDepositStore depositStore = new FakeMealDepositStore();
        depositStore.save(mealDeposit);

        List<Deposit> mealDeposits = depositStore.findAll();
        assertThat(mealDeposits)
                .isNotEmpty()
                .extracting(Deposit::getAmount, Deposit::getType)
                .containsExactly(tuple(mealAmount, "meal"));
    }

    private Deposit createGiftDeposit(MoneyAmount amount) {
        Employee john = new Employee("John");
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company total = new Company("Total", totalBalance, Set.of(john));
        CompanyDepositPerformer companyDepositPerformer = new CompanyGiftDepositPerformer(total);
        return companyDepositPerformer
                .to(john)
                .doDeposit(amount);
    }

    private Deposit createMealDeposit(MoneyAmount amount) {
        Employee john = new Employee("John");
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company total = new Company("Total", totalBalance, Set.of(john));
        CompanyDepositPerformer companyDepositPerformer = new CompanyMealDepositPerformer(total);
        return companyDepositPerformer
                .to(john)
                .doDeposit(amount);
    }
}
