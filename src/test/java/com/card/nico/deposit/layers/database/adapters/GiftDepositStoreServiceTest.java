package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.*;
import com.card.nico.deposit.layers.core.ports.in.CompanyDepositPerformer;
import com.card.nico.deposit.layers.core.ports.in.CompanyGiftDepositPerformer;
import com.card.nico.deposit.layers.core.ports.out.GiftDepositStore;
import com.card.nico.deposit.layers.database.tooling.DBTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DBTest
class GiftDepositStoreServiceTest {

    private static final String EURO_CODE = "EUR";

    @Inject
    private GiftDepositStore giftDepositStoreService;

    @Test
    @DisplayName("Can store a core gift deposit")
    void test01() {
        MoneyAmount giftAmount = MoneyAmount.of(230, EURO_CODE);
        Deposit giftDeposit = createCoreGiftDeposit(giftAmount);
        giftDepositStoreService.save(giftDeposit);

        List<GiftDeposit> giftDeposits = giftDepositStoreService.findAll();
        assertThat(giftDeposits)
                .extracting(GiftDeposit::getAmount)
                .containsExactly(giftAmount);
    }

    private Deposit createCoreGiftDeposit(MoneyAmount amount) {
        Employee john = new Employee("John");
        MoneyAmount totalBalance = MoneyAmount.of(680000d, EURO_CODE);
        Company total = new Company("Total", totalBalance, Set.of(john));
        CompanyDepositPerformer companyDepositPerformer = new CompanyGiftDepositPerformer(total);
        return companyDepositPerformer
                .to(john)
                .doDeposit(amount);
    }
}