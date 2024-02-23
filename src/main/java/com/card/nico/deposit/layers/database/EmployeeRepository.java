package com.card.nico.deposit.layers.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT ec.company FROM EmployeeCompanyEntity ec WHERE ec.employee.id = :employeeId")
    Optional<CompanyEntity> findCompanyByEmployeeId(@Param("employeeId") long employeeId);

    Optional<EmployeeEntity> findByName(String name);
}
