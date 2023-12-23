package com.example.demo.server;

import com.example.demo.business.UserBalanceRepository;
import com.example.demo.data.Deposit;
import com.example.demo.data.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserBalanceRepositoryImpl implements UserBalanceRepository {

    private final Map<User, Collection<Deposit>> deposits;

    public UserBalanceRepositoryImpl() {
        this.deposits = new HashMap<>();
    }

    @Override
    public void addDeposit(User user, Deposit deposit) {
        deposits.computeIfAbsent(user, candidate -> new ArrayList<>());
        deposits.compute(user, (candidate, list) -> {
            list.add(deposit);
            return list;
        });
    }

    @Override
    public Collection<Deposit> getDeposits(User user) {
        // Defensive copy
        return List.copyOf(deposits.getOrDefault(user, List.of()));
    }
}
