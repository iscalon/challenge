package com.example.demo.data;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class Company {

    private final long id;
    private String name;
    private BigDecimal balance;

    public Company(long id) {
        this(id, null);
    }

    public Company(long id, String name) {
        this(id, name, BigDecimal.ZERO);
    }

    public Company(long id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<BigDecimal> getBalance() {
        return Optional.ofNullable(balance);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id && Objects.equals(name, company.name) && Objects.equals(balance, company.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
