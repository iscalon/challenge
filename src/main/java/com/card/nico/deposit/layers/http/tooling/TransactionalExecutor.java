package com.card.nico.deposit.layers.http.tooling;

import com.card.nico.deposit.layers.core.ports.out.TransactionPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionalExecutor implements TransactionPort {

    @Override
    @Transactional
    public <T> T executeTransactionally(Supplier<T> supplier) {
        return supplier.get();
    }
}
