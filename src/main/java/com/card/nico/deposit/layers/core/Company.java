package com.card.nico.deposit.layers.core;

import java.util.Set;

public record Company(String name, MoneyAmount balance, Set<Employee> employees) {
}
