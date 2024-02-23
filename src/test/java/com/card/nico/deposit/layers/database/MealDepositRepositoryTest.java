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
class MealDepositRepositoryTest {

    private static final Currency EUROS = Currency.getInstance(Locale.FRANCE);

    @Inject
    private MealDepositRepository mealDepositRepository;

    @Test
    @DisplayName("Gift deposit creation should work")
    @Transactional
    void test01() {
        assertThat(mealDepositRepository.findAll())
                .isEmpty();

        EmployeeEntity employee = new EmployeeEntity("John");
        MealDepositEntity deposit = new MealDepositEntity(new MoneyAmount(BigDecimal.ZERO, EUROS), employee);
        mealDepositRepository.save(deposit);

        List<MealDepositEntity> deposits = mealDepositRepository.findAll();
        assertThat(deposits)
                .isNotEmpty()
                .extracting(MealDepositEntity::getEmployee)
                .extracting(EmployeeEntity::getName)
                .containsExactly("John");
    }
}