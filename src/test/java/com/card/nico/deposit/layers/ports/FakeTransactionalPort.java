package com.card.nico.deposit.layers.ports;

import com.card.nico.deposit.layers.core.ports.out.TransactionPort;

import java.util.function.Supplier;

public class FakeTransactionalPort implements TransactionPort {
    @Override
    public <T> T executeTransactionally(Supplier<T> supplier) {
        return supplier.get();
    }
}
