package com.example.demo.business;

import com.example.demo.data.Deposit;
import com.example.demo.data.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Service
public class UserBalances {

    private final UserBalanceRepository userBalanceRepository;

    UserBalances(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = requireNonNull(userBalanceRepository);
    }

    /**
     * Computes user's balance.
     * @param user
     * @return sum of all unexpired deposit amounts.
     */
    public BigDecimal computeBalance(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        LocalDate now = LocalDate.now();
        return userBalanceRepository.getDeposits(user).stream()
                .filter(deposit -> !deposit.expirationDate().isBefore(now))
                .map(Deposit::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
