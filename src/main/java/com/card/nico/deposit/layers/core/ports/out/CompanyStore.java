package com.card.nico.deposit.layers.core.ports.out;

import com.card.nico.deposit.layers.core.Company;

import java.util.Collection;
import java.util.Optional;

public interface CompanyStore {

    void save(Company company);

    Optional<Company> findByName(String name);

    Collection<Company> findAll();
}
