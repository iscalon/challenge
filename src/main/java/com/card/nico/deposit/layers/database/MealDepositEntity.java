package com.card.nico.deposit.layers.database;

import com.card.nico.deposit.layers.core.MoneyAmount;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;


@Entity
@Table(name = "meal_deposit")
public class MealDepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Access(AccessType.FIELD)
    private BigDecimal amount;
    @Convert(converter = CurrencyConverter.class)
    @Access(AccessType.FIELD)
    private Currency currency;
    @Access(AccessType.FIELD)
    private LocalDate expirationDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @CreationTimestamp
    private LocalDateTime creationDate;
    @Version
    @Access(AccessType.FIELD)
    private Long version;

    protected MealDepositEntity() {
        this(null, null);
    }

    public MealDepositEntity(MoneyAmount moneyAmount, EmployeeEntity employee) {
        this.employee = employee;
        if(moneyAmount == null) {
            return;
        }
        this.amount = moneyAmount.amount();
        this.currency = moneyAmount.currency();
    }

    public Long getId() {
        return id;
    }

    public MoneyAmount getMoneyAmount() {
        return new MoneyAmount(this.amount, this.currency);
    }

    public Optional<LocalDate> getExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
