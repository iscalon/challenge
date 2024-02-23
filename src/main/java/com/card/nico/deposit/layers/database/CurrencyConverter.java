package com.card.nico.deposit.layers.database;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

@Converter(autoApply = false)
class CurrencyConverter implements AttributeConverter<Currency, String> {

    @Override
    public String convertToDatabaseColumn(Currency currency) {
        return Optional.ofNullable(currency)
                .map(Currency::getCurrencyCode)
                .orElseGet(() -> Currency.getInstance(Locale.FRANCE).getCurrencyCode());
    }

    @Override
    public Currency convertToEntityAttribute(String currencyCode) {
        return Optional.ofNullable(currencyCode)
                .map(Currency::getInstance)
                .orElse(Currency.getInstance(Locale.FRANCE));
    }
}
