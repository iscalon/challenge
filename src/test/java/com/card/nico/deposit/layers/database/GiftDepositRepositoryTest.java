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

import static org.assertj.core.api.Assertions.assertThat;

@DBTest
class GiftDepositRepositoryTest {

    @Inject
    private GiftDepositRepository giftDepositRepository;

    @Test
    @DisplayName("Gift deposit creation should work")
    @Transactional
    void test01() {
        assertThat(giftDepositRepository.findAll())
                .isEmpty();

        EmployeeEntity employee = new EmployeeEntity("John");
        GiftDepositEntity deposit = new GiftDepositEntity(new MoneyAmount(BigDecimal.ZERO, Currency.getInstance(Locale.FRANCE)), employee);
        giftDepositRepository.save(deposit);

        List<GiftDepositEntity> deposits = giftDepositRepository.findAll();
        assertThat(deposits)
                .isNotEmpty()
                .extracting(GiftDepositEntity::getEmployee)
                .extracting(EmployeeEntity::getName)
                .containsExactly("John");
    }
}