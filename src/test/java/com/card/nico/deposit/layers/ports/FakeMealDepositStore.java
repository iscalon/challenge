package com.card.nico.deposit.layers.ports;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.ports.out.DepositStore;

import java.util.*;

public class FakeMealDepositStore implements DepositStore {

    private final Collection<Deposit> deposits;

    public FakeMealDepositStore() {
        this.deposits = new HashSet<>();
    }

    @Override
    public Optional<Deposit> findById(Long id) {
        return deposits.stream().filter(deposit -> Objects.equals(deposit.getId(), id)).findAny();
    }

    @Override
    public List<Deposit> findByEmployeeName(String employeeName) {
        return this.deposits.stream()
                .filter(deposit -> Objects.equals(deposit.getEmployee().name(), employeeName))
                .toList();
    }

    @Override
    public Deposit save(Deposit deposit) {
        this.deposits.add(deposit);
        return deposit;
    }

    @Override
    public List<Deposit> findAll() {
        return List.copyOf(deposits);
    }

    @Override
    public String type() {
        return "MEAL";
    }

    public void clear() {
        this.deposits.clear();
    }
}
