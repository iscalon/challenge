package com.card.nico.deposit.layers.core.exceptions;

public class CompanyNotFoundException extends DepositCoreException {

    public CompanyNotFoundException(String companyName) {
        super(companyName + " does not exist");
    }
}
