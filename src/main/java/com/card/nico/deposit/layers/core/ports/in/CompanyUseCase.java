package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;

import java.util.Collection;
import java.util.Optional;

public interface CompanyUseCase {

    void save(Company company);

    Optional<Company> findByName(String name);

    Optional<Company> findByEmployeeName(String name);

    Collection<Company> findAll();
}
