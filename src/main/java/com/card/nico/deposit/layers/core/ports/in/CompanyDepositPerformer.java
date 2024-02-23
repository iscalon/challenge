package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Employee;

public interface CompanyDepositPerformer {

    CompanyToEmployeeDepositPerformer to(Employee employee);
}
