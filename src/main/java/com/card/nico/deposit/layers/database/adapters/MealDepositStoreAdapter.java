package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MealDeposit;
import com.card.nico.deposit.layers.core.ports.out.DepositStore;
import com.card.nico.deposit.layers.database.MealDepositEntity;
import com.card.nico.deposit.layers.database.MealDepositRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
class MealDepositStoreAdapter implements DepositStore {

    private final MealDepositRepository mealDepositRepository;
    private final MealDepositEntityConverter mealDepositEntityConverter;

    MealDepositStoreAdapter(MealDepositRepository mealDepositRepository,
                            MealDepositEntityConverter mealDepositEntityConverter) {
        this.mealDepositRepository = requireNonNull(mealDepositRepository);
        this.mealDepositEntityConverter = requireNonNull(mealDepositEntityConverter);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<MealDeposit> findById(Long id) {
        return mealDepositRepository.findById(id)
                .map(mealDepositEntityConverter::toCoreMealDeposit);
    }

    @Transactional
    @Override
    public Deposit save(Deposit deposit) {
        MealDepositEntity giftDepositEntity = this.mealDepositEntityConverter.fromCoreMealDeposit(deposit);
        return mealDepositEntityConverter.toCoreMealDeposit(
                mealDepositRepository.save(giftDepositEntity));
    }

    @Transactional(readOnly = true)
    @Override
    public List<MealDeposit> findByEmployeeName(String employeeName) {
        return mealDepositRepository.findByEmployeeName(employeeName).stream()
                .map(mealDepositEntityConverter::toCoreMealDeposit).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MealDeposit> findAll() {
        return mealDepositRepository.findAll().stream()
                .map(mealDepositEntityConverter::toCoreMealDeposit)
                .toList();
    }

    @Override
    public String type() {
        return "MEAL";
    }
}
