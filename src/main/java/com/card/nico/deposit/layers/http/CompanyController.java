package com.card.nico.deposit.layers.http;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.exceptions.CompanyNotFoundException;
import com.card.nico.deposit.layers.core.exceptions.EmployeeNotFoundException;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/companies")
class CompanyController {

    private final CompanyStore companyStore;
    private final EmployeeStore employeeStore;

    CompanyController(CompanyStore companyStore, EmployeeStore employeeStore) {
        this.companyStore = requireNonNull(companyStore);
        this.employeeStore = requireNonNull(employeeStore);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        Optional<Representation> companyRepresentation = this.companyStore.findByName(name)
                .map(Representation::new);

        return ResponseEntity.of(companyRepresentation);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping
    public ResponseEntity<?> list() {
        List<Representation> representations = this.companyStore.findAll().stream()
                .map(Representation::new)
                .toList();

        CollectionModel<Representation> collectionModel = CollectionModel.of(representations);
        Link selfLink = linkTo(methodOn(CompanyController.class).list())
                .withSelfRel()
                .andAffordance(afford(methodOn(CompanyController.class).create(null)));
        return ResponseEntity.ok(
                collectionModel.add(selfLink));
    }

    @SuppressWarnings("java:S1452")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCommand command) {
        Set<String> employeesNames = Stream.of(command.employeesNames()).collect(Collectors.toSet());
        Set<Employee> employees = findOrCreateEmployees(employeesNames);

        String companyName = command.companyName();
        companyStore.save(new Company(companyName, MoneyAmount.of(command.amount(), command.currencyCode()), employees));
        return ResponseEntity.created(
                linkTo(methodOn(CompanyController.class).findByName(companyName)).toUri())
                .build();
    }

    @SuppressWarnings("java:S1452")
    @PutMapping("/company/{companyName}/employee")
    public ResponseEntity<?> addEmployee(@PathVariable String companyName, @RequestBody EmployeeCommand command) {
        String employeeName = command.employeeName();
        Employee employee = employeeStore.findByName(employeeName)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeName));
        Company company = companyStore.findByName(companyName)
                .orElseThrow(() -> new CompanyNotFoundException(companyName));

        Set<Employee> companyEmployees = new HashSet<>(company.employees());
        companyEmployees.add(employee);
        companyStore.save(new Company(companyName, company.balance(), companyEmployees));

        return ResponseEntity.noContent().build();
    }

    private Set<Employee> findOrCreateEmployees(Set<String> employeesNames) {
        employeesNames.stream()
                .map(Employee::new)
                .forEach(employeeStore::save);
        return employeesNames.stream()
                .map(employeeStore::findByName)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    record CreateCommand(String companyName, Double amount, String currencyCode, String... employeesNames) {

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    record EmployeeCommand(String employeeName) {}

    static class Representation extends RepresentationModel<Representation> {

        private final String name;
        private final String balance;
        private final Set<String> employeeNames;

        Representation(Company company) {
            this.name = company.name();
            MoneyAmount companyBalance = company.balance();
            this.balance = companyBalance.amount() + " " + companyBalance.currency().getCurrencyCode();
            this.employeeNames = company.employees().stream()
                    .map(Employee::name)
                    .collect(Collectors.toSet());

            addSelfLink();
            addEmployeesLink();
        }

        private void addSelfLink() {
            this.add(linkTo(methodOn(CompanyController.class).findByName(name)).withSelfRel()
                    .andAffordance(staffingAffordance()));
        }

        private Affordance staffingAffordance() {
            return afford(methodOn(CompanyController.class).addEmployee(name, null));
        }

        private void addEmployeesLink() {
            employeeNames.forEach(employeeName ->
                    this.add(linkTo(methodOn(EmployeeController.class).findByName(employeeName)).withRel("employees")));
        }

        public String getName() {
            return name;
        }

        public String getBalance() {
            return balance;
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
