package com.example.demo.business;

import com.example.demo.data.GiftDeposit;
import com.example.demo.data.MealDeposit;
import com.example.demo.data.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserBalancesUnitTest {

    @Test
    @DisplayName("""
            GIVEN three deposits made to an user : two with unexpired expiration dates and one with expired expiration date
            WHEN requesting balance for this user
            THEN the returned amount is 3 that is corresponding to the two unexpired deposits
            """)
    void test01() {
        LocalDate now = LocalDate.now();
        GiftDeposit giftDeposit1 = new GiftDeposit(BigDecimal.ONE, now.plusYears(2));
        GiftDeposit giftDeposit2 = new GiftDeposit(BigDecimal.TEN, now.minusYears(7));
        MealDeposit mealDeposit = new MealDeposit(BigDecimal.TWO, now.plusYears(2));
        UserBalanceRepository userBalanceRepository = Mockito.mock(UserBalanceRepository.class);
        when(userBalanceRepository.getDeposits(any(User.class)))
                .thenReturn(List.of(giftDeposit1, giftDeposit2, mealDeposit));

        UserBalances userBalances = new UserBalances(userBalanceRepository);
        BigDecimal balance = userBalances.computeBalance(new User(-1));
        assertThat(balance).isEqualTo(BigDecimal.valueOf(3));
    }
}