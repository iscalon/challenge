package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.*;
import com.card.nico.deposit.layers.core.ports.in.CompanyUseCase;
import com.card.nico.deposit.layers.core.ports.in.DepositUseCase;
import com.card.nico.deposit.layers.core.ports.in.EmployeeUseCase;
import com.card.nico.deposit.layers.database.tooling.DBTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DBTest
class GiftDepositStoreAdapterTest {

    private static final String EURO_CODE = "EUR";

    @Inject
    private DepositUseCase deposits;

    @Inject
    private EmployeeUseCase employees;

    @Inject
    private CompanyUseCase companies;

    @Test
    @DisplayName("Can store a core gift deposit")
    @Transactional
    void test01() {
        MoneyAmount giftAmount = MoneyAmount.of(230, EURO_CODE);
        Deposit giftDeposit = createCoreGiftDeposit(giftAmount);
        deposits.type("GIFT").save(giftDeposit);

        List<GiftDeposit> giftDeposits = deposits.type("GIFT").findAll();
        assertThat(giftDeposits)
                .extracting(GiftDeposit::getAmount)
                .containsExactly(giftAmount);
    }

    private Deposit createCoreGiftDeposit(MoneyAmount amount) {
        Employee john = new Employee("John");
        employees.save(john);
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company company = new Company("Total", totalBalance, Set.of(john));
        companies.save(company);
        return deposits.type("GIFT")
                .from(company.name())
                .to(john.name())
                .doDeposit(amount);
    }
}