package com.card.nico.deposit.layers.http;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.GiftDeposit;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.ports.in.DepositPerformer;
import com.card.nico.deposit.layers.core.ports.out.GiftDepositStore;
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
@RequestMapping("/gift-deposits")
class GiftDepositController {

    private final GiftDepositStore giftDepositStore;
    private final DepositPerformer depositPerformer;

    GiftDepositController(GiftDepositStore giftDepositStore, DepositPerformer depositPerformer) {
        this.giftDepositStore = requireNonNull(giftDepositStore);
        this.depositPerformer = requireNonNull(depositPerformer);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Representation> giftDepositRepresentation = this.giftDepositStore.findById(id)
                .map(GiftDeposit.class::cast)
                .map(Representation::new);

        return ResponseEntity.of(giftDepositRepresentation);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping
    public ResponseEntity<?> list() {
        List<Representation> representations = this.giftDepositStore.findAll().stream()
                .map(GiftDeposit.class::cast)
                .map(Representation::new)
                .toList();

        CollectionModel<Representation> collectionModel = CollectionModel.of(representations);
        Link selfLink = linkTo(methodOn(GiftDepositController.class).list()).withSelfRel()
                .andAffordance(afford(methodOn(GiftDepositController.class).create(null)));

        return ResponseEntity.ok(
                collectionModel.add(selfLink));
    }

    @SuppressWarnings("java:S1452")
    @GetMapping("/employee/{name}")
    public ResponseEntity<?> findByEmployee(@PathVariable("name") String employeeName) {
        List<Representation> representations = this.giftDepositStore.findByEmployeeName(employeeName).stream()
                .map(GiftDeposit.class::cast)
                .map(Representation::new)
                .toList();

        CollectionModel<Representation> collectionModel = CollectionModel.of(representations);
        Link selfLink = linkTo(methodOn(GiftDepositController.class).findByEmployee(employeeName)).withSelfRel();

        return ResponseEntity.ok(
                collectionModel.add(selfLink));
    }

    @SuppressWarnings("java:S1452")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCommand command) {
        MoneyAmount amount = MoneyAmount.of(command.amount(), command.currencyCode());
        Deposit deposit = depositPerformer
                .type("GIFT")
                .from(command.companyName())
                .to(command.employeeName())
                .doDeposit(amount);
        return ResponseEntity.created(
                        linkTo(methodOn(GiftDepositController.class).findById(deposit.getId())).toUri())
                .build();
    }

    record CreateCommand(String companyName, String employeeName, double amount, String currencyCode) {}

    static class Representation extends RepresentationModel<Representation> {

        private final Long id;
        private final String companyName;
        private final String employeeName;
        private final String amount;
        private final String expirationDate;
        private final String creationDate;

        Representation(GiftDeposit giftDeposit) {
            this.id = giftDeposit.getId();
            this.companyName = giftDeposit.getCompany().name();
            this.employeeName = giftDeposit.getEmployee().name();
            MoneyAmount moneyAmount = giftDeposit.getAmount();
            this.amount = moneyAmount.amount() + moneyAmount.currency().getCurrencyCode();
            this.expirationDate = "" + giftDeposit.expirationDate();
            this.creationDate = "" + giftDeposit.getCreationDate();

            addSelfLink();
            addEmployeeLink();
            addCompanyLink();
        }

        private void addSelfLink() {
            this.add(linkTo(methodOn(GiftDepositController.class).findById(id)).withSelfRel());
        }

        private void addCompanyLink() {
            this.add(linkTo(methodOn(CompanyController.class).findByName(companyName)).withRel("company"));
        }

        private void addEmployeeLink() {
            this.add(linkTo(methodOn(EmployeeController.class).findByName(employeeName)).withRel("employee"));
        }

        public String getAmount() {
            return amount;
        }

        public String getExpirationDate() {
            return expirationDate;
        }

        public String getCreationDate() {
            return creationDate;
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
