package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.*;
import com.card.nico.deposit.layers.database.EmployeeEntity;
import com.card.nico.deposit.layers.database.EmployeeRepository;
import com.card.nico.deposit.layers.database.GiftDepositEntity;
import com.card.nico.deposit.layers.database.GiftDepositRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
class GiftDepositEntityConverter {

    private final GiftDepositRepository giftDepositRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeEntityConverter employeeEntityConverter;
    private final CompanyEntityConverter companyEntityConverter;

    GiftDepositEntityConverter(GiftDepositRepository giftDepositRepository, EmployeeRepository employeeRepository, EmployeeEntityConverter employeeEntityConverter, CompanyEntityConverter companyEntityConverter) {
        this.giftDepositRepository = requireNonNull(giftDepositRepository);
        this.employeeRepository = requireNonNull(employeeRepository);
        this.employeeEntityConverter = requireNonNull(employeeEntityConverter);
        this.companyEntityConverter = requireNonNull(companyEntityConverter);
    }

    @Transactional
    public GiftDepositEntity fromCoreGiftDeposit(Deposit giftDeposit) {
        if(giftDeposit == null) {
            return null;
        }

        Optional<GiftDepositEntity> giftDepositEntity = this.giftDepositRepository.findByCreationDate(giftDeposit.getCreationDate());
        return giftDepositEntity
                .orElseGet(() -> createAndSaveGiftDeposit(giftDeposit));
    }

    private GiftDepositEntity createAndSaveGiftDeposit(Deposit giftDeposit) {
        MoneyAmount amount = giftDeposit.getAmount();
        EmployeeEntity employee = employeeEntityConverter.fromCoreEmployee(giftDeposit.getEmployee());
        createCompanyEntityIfNeeded(giftDeposit.getCompany());
        GiftDepositEntity giftDepositEntity = new GiftDepositEntity(amount, employee);
        giftDeposit.getExpirationDate()
                .ifPresent(giftDepositEntity::setExpirationDate);
        return giftDepositRepository.save(giftDepositEntity);
    }

    private void createCompanyEntityIfNeeded(Company company) {
        companyEntityConverter.fromCoreCompany(company);
    }

    @Transactional
    public GiftDeposit toCoreGiftDeposit(GiftDepositEntity giftDepositEntity) {
        if(giftDepositEntity == null) {
            return null;
        }
        MoneyAmount amount = new MoneyAmount(giftDepositEntity.getAmount(), giftDepositEntity.getCurrency());
        EmployeeEntity employeeEntity = giftDepositEntity.getEmployee();
        Employee employee = employeeEntityConverter.toCoreEmployee(employeeEntity);
        Company company = employeeRepository.findCompanyByEmployeeId(employeeEntity.getId())
                .map(companyEntityConverter::toCoreCompany)
                .orElseThrow();
        LocalDateTime creationDate = giftDepositEntity.getCreationDate();
        LocalDate expirationDate = giftDepositEntity.getExpirationDate().orElse(null);
        return new GiftDeposit(giftDepositEntity.getId(), amount,company,employee, creationDate,expirationDate);
    }
}
