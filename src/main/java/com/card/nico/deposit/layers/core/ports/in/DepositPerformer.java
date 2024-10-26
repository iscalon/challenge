package com.card.nico.deposit.layers.core.ports.in;

public interface DepositPerformer {

    DepositKindPerformer type(String depositType);
}
