package com.card.nico.deposit.layers.database;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Access(AccessType.FIELD)
    private String name;
    @Version
    @Access(AccessType.FIELD)
    private Long version;

    protected EmployeeEntity() {
        this(null);
    }

    public EmployeeEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
