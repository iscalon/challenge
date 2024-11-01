package com.card.nico.deposit.layers.http;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.ports.in.CompanyUseCase;
import com.card.nico.deposit.layers.core.ports.in.EmployeeUseCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/employees")
class EmployeeController {

    private final EmployeeUseCase employees;
    private final CompanyUseCase companies;

    EmployeeController(EmployeeUseCase employees, CompanyUseCase companies) {
        this.employees = requireNonNull(employees);
        this.companies = requireNonNull(companies);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        Optional<Representation> employeeRepresentation = this.employees.findByName(name)
                .map(Representation::new);

        return ResponseEntity.of(employeeRepresentation);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping
    public ResponseEntity<?> list() {
        List<Representation> representations = this.employees.findAll().stream()
                .map(Representation::new)
                .toList();

        CollectionModel<Representation> collectionModel = CollectionModel.of(representations);
        Link selfLink = linkTo(methodOn(EmployeeController.class).list()).withSelfRel()
                .andAffordance(afford(methodOn(EmployeeController.class).create(null)));
        return ResponseEntity.ok(
                collectionModel.add(selfLink));
    }

    @SuppressWarnings("java:S1452")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCommand command) {
        String employeeName = command.employeeName();
        employees.save(new Employee(employeeName));
        return ResponseEntity.created(
                        linkTo(methodOn(EmployeeController.class).findByName(employeeName)).toUri())
                .build();
    }

    record CreateCommand(String employeeName) {}

    class Representation extends RepresentationModel<Representation> {

        private final String name;

        Representation(Employee employee) {
            this.name = employee.name();

            addSelfLink();
            addCompanyLink();
            addGiftDepositsLink();
            addMealDepositsLink();
        }

        private void addSelfLink() {
            this.add(linkTo(methodOn(EmployeeController.class).findByName(name)).withSelfRel());
        }

        private void addCompanyLink() {
            Optional<String> companyName = companies.findByEmployeeName(name)
                    .map(Company::name);

            companyName.ifPresent(candidate ->
                this.add(linkTo(methodOn(CompanyController.class).findByName(candidate)).withRel("company")));
        }

        private void addGiftDepositsLink() {
            this.add(linkTo(methodOn(GiftDepositController.class).findByEmployee(name)).withRel("gift-deposits"));
        }

        private void addMealDepositsLink() {
            this.add(linkTo(methodOn(MealDepositController.class).findByEmployee(name)).withRel("meal-deposits"));
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }
    }
}
