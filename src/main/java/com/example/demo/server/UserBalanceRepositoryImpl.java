package com.example.demo.server;

import com.example.demo.business.UserBalanceRepository;
import com.example.demo.data.Deposit;
import com.example.demo.data.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserBalanceRepositoryImpl implements UserBalanceRepository {

    /**
     * @param user    the user who receives the deposit.
     * @param deposit
     */
    @Override
    public void addDeposit(User user, Deposit deposit) {
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * @param user the user from who we want to retrieve deposits.
     * @return
     */
    @Override
    public Collection<Deposit> getDeposits(User user) {
        throw new RuntimeException("Not yet implemented");
    }
}
