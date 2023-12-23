package com.example.demo.business;

import com.example.demo.data.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

@Service
public class UserBalances {

    private final UserBalanceRepository userBalanceRepository;

    UserBalances(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = requireNonNull(userBalanceRepository);
    }

    public BigDecimal computeBalance(User user) {
        throw new RuntimeException("Not yet implemented");
    }
}
