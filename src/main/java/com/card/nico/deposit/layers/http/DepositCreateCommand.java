package com.card.nico.deposit.layers.http;

public record DepositCreateCommand(String companyName, String employeeName, double amount, String currencyCode) {
}
