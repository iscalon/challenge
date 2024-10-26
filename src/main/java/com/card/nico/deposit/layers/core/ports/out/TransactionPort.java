package com.card.nico.deposit.layers.core.ports.out;

import java.util.function.Supplier;

public interface TransactionPort {

    <T> T executeTransactionally(Supplier<T> supplier);
}
