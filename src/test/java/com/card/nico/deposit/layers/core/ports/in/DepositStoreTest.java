package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.ports.out.DepositStore;
import com.card.nico.deposit.layers.ports.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class DepositStoreTest {

    public static final String EURO_CODE = "EUR";

    private final FakeCompanyStore companyStore = new FakeCompanyStore();
    private final FakeEmployeeStore employeeStore = new FakeEmployeeStore();
    private final FakeGiftDepositStore giftDepositStore = new FakeGiftDepositStore();
    private final FakeMealDepositStore mealDepositStore = new FakeMealDepositStore();
    private final List<DepositStore> depositStores = List.of(giftDepositStore, mealDepositStore);
    private final List<DepositStrategy> depositStrategies = List.of(
            new GiftDepositStrategy(companyStore, employeeStore, depositStores),
            new MealDepositStrategy(companyStore, employeeStore, depositStores)
    );
    private final DepositUseCase deposits = new DepositPerformerFactory(depositStrategies, companyStore,
            new FakeTransactionalPort());

    @BeforeEach
    void setUp() {
        companyStore.clear();
        employeeStore.clear();
        giftDepositStore.clear();
        mealDepositStore.clear();
        employeeStore.setCompany(null);
    }

    @Test
    @DisplayName("Can store a gift deposit")
    void test01() {
        MoneyAmount giftAmount = MoneyAmount.of(660, EURO_CODE);
        makeGiftDeposit(giftAmount);

        List<Deposit> giftDeposits = deposits.type("GIFT").findAll();
        assertThat(giftDeposits)
                .isNotEmpty()
                .extracting(Deposit::getAmount, Deposit::getType)
                .containsExactly(tuple(giftAmount, "GIFT"));
    }

    @Test
    @DisplayName("Can store a meal deposit")
    void test02() {
        MoneyAmount mealAmount = MoneyAmount.of(660, EURO_CODE);
        makeMealDeposit(mealAmount);

        List<Deposit> mealDeposits = deposits.type("MEAL").findAll();
        assertThat(mealDeposits)
                .isNotEmpty()
                .extracting(Deposit::getAmount, Deposit::getType)
                .containsExactly(tuple(mealAmount, "MEAL"));
    }

    private void makeGiftDeposit(MoneyAmount amount) {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        deposits
                .type("GIFT")
                .from(company.name())
                .to(john.name())
                .doDeposit(amount);
    }

    private void makeMealDeposit(MoneyAmount amount) {
        Employee john = new Employee("John");
        employeeStore.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companyStore.save(company);
        employeeStore.setCompany(company);
        deposits
                .type("MEAL")
                .from(company.name())
                .to(john.name())
                .doDeposit(amount);
    }
}
