package com.card.nico.deposit.layers.ports;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;

import java.util.*;

public class FakeCompanyStore implements CompanyStore {

    private final Collection<Company> companies;

    public FakeCompanyStore() {
        this.companies = new HashSet<>();
    }

    @Override
    public void save(Company company) {
        this.companies.add(company);
    }

    @Override
    public Optional<Company> findByName(String name) {
        return this.companies.stream()
                .filter(company -> Objects.equals(company.name(), name))
                .findAny();
    }

    @Override
    public Collection<Company> findAll() {
        return List.copyOf(this.companies);
    }

    public void clear() {
        this.companies.clear();
    }
}
