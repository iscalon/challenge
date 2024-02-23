package com.card.nico.deposit.layers.core;

import com.card.nico.deposit.layers.core.exceptions.DepositCoreException;
import jakarta.annotation.Nonnull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record MoneyAmount(@Nonnull BigDecimal amount, @Nonnull Currency currency) {

    public MoneyAmount(@Nonnull BigDecimal amount, @Nonnull Currency currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency;
    }

    private MoneyAmount(double amount, @Nonnull String currencyCode) {
        this(BigDecimal.valueOf(amount), Currency.getInstance(currencyCode));
    }

    public static MoneyAmount of(double amount, String currencyCode) {
        return new MoneyAmount(amount, currencyCode);
    }

    public MoneyAmount minus(MoneyAmount amount) {
        if(amount == null) {
            return this;
        }
        String currencyCode = this.currency().getCurrencyCode();
        String otherCurrencyCode = amount.currency().getCurrencyCode();
        if(!Objects.equals(currencyCode, otherCurrencyCode)) {
            throw new DepositCoreException("Cannot subtract amount because currency : " + currencyCode + " is different from : " + otherCurrencyCode);
        }

        return new MoneyAmount(this.amount().subtract(amount.amount()), this.currency());
    }
}
