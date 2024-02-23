package com.card.nico.deposit.layers.database;

import com.card.nico.deposit.layers.core.MoneyAmount;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final String name;
    @Access(AccessType.FIELD)
    private BigDecimal balance;
    @Convert(converter = CurrencyConverter.class)
    @Access(AccessType.FIELD)
    private Currency currency;
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "employee_company",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<EmployeeEntity> employees = new HashSet<>();
    @Version
    @Access(AccessType.FIELD)
    private Long version;

    protected CompanyEntity() {
        this(null);
    }

    public CompanyEntity(String name) {
        this(name, MoneyAmount.of(0, "EUR"));
    }

    public CompanyEntity(String name, MoneyAmount balance) {
        this.name = name;
        this.balance = balance.amount();
        this.currency = balance.currency();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setBalanceAmount(MoneyAmount balanceAmount) {
        this.balance = balanceAmount.amount();
        this.currency = balanceAmount.currency();
    }

    public MoneyAmount getBalanceAmount() {
        return new MoneyAmount(this.balance, this.currency);
    }

    public void addEmployee(EmployeeEntity employee) {
        this.employees.add(employee);
    }

    public Set<EmployeeEntity> getEmployees() {
        return Set.copyOf(employees);
    }
}
