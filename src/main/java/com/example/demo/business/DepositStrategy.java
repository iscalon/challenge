package com.example.demo.business;

import com.example.demo.data.Company;
import com.example.demo.data.Deposit;
import com.example.demo.data.User;

import java.math.BigDecimal;

/**
 * The strategy used to perform the deposit.
 */
public interface DepositStrategy {

    /**
     * @return the strategy type name (e.g. : "gift" or "meal").
     */
    String getTypeName();

    /**
     * Performs the deposit action.
     *
     * @param company the company that makes the deposit.
     * @param user the user who receives the deposit.
     * @param amount the deposit amount.
     * @return the resulting deposit.
     */
    Deposit execute(Company company, User user, BigDecimal amount);
}
