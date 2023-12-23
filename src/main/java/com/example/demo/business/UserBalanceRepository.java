package com.example.demo.business;

import com.example.demo.data.Deposit;
import com.example.demo.data.User;

import java.util.Collection;

/**
 * Interface allowing to handle user deposits.<br>
 * <p>The goal of this interface is to allow "dependency inversion" principle and avoid dependencies from the business layer going out to lower layers.</p>
 * <p>It will also be easier to perform tests by giving an implementation to replace the real implementation.</p>
 *
 */
public interface UserBalanceRepository {

    /**
     * Registers a deposit for a given user.
     *
     * @param user the user who receives the deposit.
     * @param deposit
     */
    void addDeposit(User user, Deposit deposit);

    /**
     * @param user the user from who we want to retrieve deposits.
     * @return all deposits for the given user.
     */
    Collection<Deposit> getDeposits(User user);
}
