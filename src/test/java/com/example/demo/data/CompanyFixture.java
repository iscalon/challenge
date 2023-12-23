package com.example.demo.data;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CompanyFixture {

    private long idGenerator = 0;

    public Company create(String name) {
        return create(name, BigDecimal.ZERO);
    }

    public Company create(String name, BigDecimal balance) {
        return new Company(++idGenerator, name, balance);
    }
}
