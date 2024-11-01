package com.card.nico.deposit.layers.core.ports.in;

public interface DepositUseCase {

    DepositKindPerformer type(String depositType);
}
