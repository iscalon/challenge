package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.GiftDeposit;
import com.card.nico.deposit.layers.core.ports.out.GiftDepositStore;
import com.card.nico.deposit.layers.database.GiftDepositEntity;
import com.card.nico.deposit.layers.database.GiftDepositRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
class GiftDepositStoreService extends GiftDepositStore {

    private final GiftDepositRepository giftDepositRepository;
    private final GiftDepositEntityConverter giftDepositEntityConverter;

    GiftDepositStoreService(GiftDepositRepository giftDepositRepository, GiftDepositEntityConverter giftDepositEntityConverter) {
        this.giftDepositRepository = requireNonNull(giftDepositRepository);
        this.giftDepositEntityConverter = requireNonNull(giftDepositEntityConverter);
    }

    @Transactional
    @Override
    public Optional<GiftDeposit> findById(Long id) {
        return giftDepositRepository.findById(id)
                .map(giftDepositEntityConverter::toCoreGiftDeposit);
    }

    @Transactional
    @Override
    public Deposit save(Deposit deposit) {
        GiftDepositEntity giftDepositEntity = this.giftDepositEntityConverter.fromCoreGiftDeposit(deposit);
        return giftDepositEntityConverter.toCoreGiftDeposit(
                giftDepositRepository.save(giftDepositEntity));
    }

    @Transactional
    @Override
    public List<GiftDeposit> findByEmployeeName(String employeeName) {
        return giftDepositRepository.findByEmployeeName(employeeName).stream().map(giftDepositEntityConverter::toCoreGiftDeposit).toList();
    }

    @Transactional
    @Override
    public List<GiftDeposit> findAll() {
        return giftDepositRepository.findAll().stream().map(giftDepositEntityConverter::toCoreGiftDeposit).toList();
    }
}
