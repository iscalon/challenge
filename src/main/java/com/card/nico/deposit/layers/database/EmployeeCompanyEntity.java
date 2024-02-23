package com.card.nico.deposit.layers.database;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_company")
class EmployeeCompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private EmployeeEntity employee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyEntity company;
    @Version
    @Access(AccessType.FIELD)
    private Long version;

    public Long getId() {
        return id;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }
}
