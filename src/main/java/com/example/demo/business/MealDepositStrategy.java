package com.example.demo.business;

import com.example.demo.data.Company;
import com.example.demo.data.Deposit;
import com.example.demo.data.GiftDeposit;
import com.example.demo.data.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

import static java.util.Objects.requireNonNull;

@Component
public class MealDepositStrategy implements DepositStrategy {

    private final UserBalanceRepository userBalanceRepository;

    private MealDepositStrategy(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = requireNonNull(userBalanceRepository);
    }

    @Override
    public String getTypeName() {
        return "meal";
    }

    @Override
    public Deposit execute(Company company, User user, BigDecimal amount) {
        BigDecimal companyBalance = company.getBalance()
                .filter(balance -> balance.compareTo(amount) >= 0)
                .orElseThrow(() -> new IllegalArgumentException("Meal amount : " + amount + " is greater than the company balance"));

        Deposit deposit = new GiftDeposit(amount, computeExpirationDate());
        userBalanceRepository.addDeposit(user, deposit);
        company.setBalance(companyBalance.subtract(amount));
        return deposit;
    }

    private LocalDate computeExpirationDate() {
        int nextYear = LocalDate.now().getYear() + 1;
        return LocalDate.of(nextYear, Month.FEBRUARY, 1).with(TemporalAdjusters.lastDayOfMonth());
    }
}
