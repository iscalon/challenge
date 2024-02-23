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
@Table(name = "gift_deposit")
public class GiftDepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    @Convert(converter = CurrencyConverter.class)
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

    protected GiftDepositEntity() {
        this(null, null);
    }

    public GiftDepositEntity(MoneyAmount moneyAmount, EmployeeEntity employee) {
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

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
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
