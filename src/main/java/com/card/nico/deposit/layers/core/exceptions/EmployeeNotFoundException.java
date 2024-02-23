package com.card.nico.deposit.layers.core.exceptions;

public class EmployeeNotFoundException extends DepositCoreException {

    public EmployeeNotFoundException(String employeeName) {
        super(employeeName + " does not exist");
    }
}
