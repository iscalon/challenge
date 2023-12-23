package com.example.demo.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Deposit {

    BigDecimal amount();

    LocalDate expirationDate();
}
