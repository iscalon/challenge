package com.card.nico.deposit.layers.database.adapters;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;
import com.card.nico.deposit.layers.database.CompanyRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Component
class CompanyStoreService implements CompanyStore {

    private final CompanyRepository companyRepository;
    private final CompanyEntityConverter companyEntityConverter;

    CompanyStoreService(CompanyRepository companyRepository, CompanyEntityConverter companyEntityConverter) {
        this.companyRepository = requireNonNull(companyRepository);
        this.companyEntityConverter = requireNonNull(companyEntityConverter);
    }

    @Override
    public void save(Company company) {
        requireNonNull(company);
        this.companyRepository.save(companyEntityConverter.fromCoreCompany(company));
    }

    @Transactional
    @Override
    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name)
                .map(companyEntityConverter::toCoreCompany);
    }

    @Transactional
    @Override
    public Collection<Company> findAll() {
        return companyRepository
                .findAll()
                .stream()
                .map(companyEntityConverter::toCoreCompany)
                .collect(Collectors.toSet());
    }


}
