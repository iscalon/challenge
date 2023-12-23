package com.example.demo.business;

import com.example.demo.data.Company;
import com.example.demo.data.Deposit;
import com.example.demo.data.GiftDeposit;
import com.example.demo.data.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Component
public class GiftDepositStrategy implements DepositStrategy {

    private final UserBalanceRepository userBalanceRepository;

    public GiftDepositStrategy(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = requireNonNull(userBalanceRepository);
    }

    @Override
    public String getTypeName() {
        return "gift";
    }

    @Override
    public Deposit execute(Company company, User user, BigDecimal amount) {
        BigDecimal companyBalance = company.getBalance().filter(balance -> balance.compareTo(amount) >= 0).orElseThrow(() -> new IllegalArgumentException("Gift amount : " + amount + " is greater than the company balance"));

        Deposit deposit = new GiftDeposit(amount, computeExpirationDate());
        userBalanceRepository.addDeposit(user, deposit);
        company.setBalance(companyBalance.subtract(amount));
        return deposit;
    }

    private LocalDate computeExpirationDate() {
        return LocalDate.now().plusDays(365);
    }
}
