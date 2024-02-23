package com.card.nico.deposit.layers.http;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.ports.out.EmployeeStore;
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

    private final EmployeeStore employeeStore;

    EmployeeController(EmployeeStore employeeStore) {
        this.employeeStore = requireNonNull(employeeStore);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        Optional<Representation> employeeRepresentation = this.employeeStore.findByName(name)
                .map(Representation::new);

        return ResponseEntity.of(employeeRepresentation);
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Representation> representations = this.employeeStore.findAll().stream()
                .map(Representation::new)
                .toList();

        CollectionModel<Representation> collectionModel = CollectionModel.of(representations);
        Link selfLink = linkTo(methodOn(EmployeeController.class).list()).withSelfRel()
                .andAffordance(afford(methodOn(EmployeeController.class).create(null)));
        return ResponseEntity.ok(
                collectionModel.add(selfLink));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCommand command) {
        String employeeName = command.employeeName();
        employeeStore.save(new Employee(employeeName));
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
            Optional<String> companyName = employeeStore.findCompany(name)
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
    }
}
