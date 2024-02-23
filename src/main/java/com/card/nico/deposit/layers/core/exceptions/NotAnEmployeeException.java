package com.card.nico.deposit.layers.core.exceptions;

public class NotAnEmployeeException extends  DepositCoreException {

    public NotAnEmployeeException(String companyName, String employeeName) {
        super(employeeName + " is not an employee of " + companyName);
    }
}
