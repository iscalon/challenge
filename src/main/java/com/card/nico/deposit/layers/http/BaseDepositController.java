package com.card.nico.deposit.layers.http;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.ports.in.DepositUseCase;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public abstract class BaseDepositController {

    private DepositUseCase deposits;

    protected BaseDepositController(DepositUseCase deposits) {
        this.deposits = requireNonNull(deposits);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<?> giftDepositRepresentation = this.deposits
                .type(handledDepositType())
                .findById(id)
                .map(this::createDepositRepresentation);

        return ResponseEntity.of(giftDepositRepresentation);
    }

    @SuppressWarnings("java:S1452")
    @GetMapping
    public ResponseEntity<?> list() {
        List<?> representations = this.deposits
                .type(handledDepositType()).findAll().stream()
                .map(this::createDepositRepresentation)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(representations)
                        .add(createListLinkAndAffordances()));
    }

    @SuppressWarnings("java:S1452")
    @GetMapping("/employee/{name}")
    public ResponseEntity<?> findByEmployee(@PathVariable("name") String employeeName) {
        List<?> representations = this.deposits.type(handledDepositType())
                .findByEmployeeName(employeeName).stream()
                .map(this::createDepositRepresentation)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(representations)
                        .add(createFindByEmployeeLinkAndAffordances(employeeName)));
    }

    @SuppressWarnings("java:S1452")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DepositCreateCommand command) {
        MoneyAmount amount = MoneyAmount.of(command.amount(), command.currencyCode());
        Deposit deposit = deposits
                .type(handledDepositType())
                .from(command.companyName())
                .to(command.employeeName())
                .doDeposit(amount);
        return ResponseEntity.created(
                        getDepositFindByIdLinkBuilder(deposit).toUri())
                .build();
    }

    abstract Link createFindByEmployeeLinkAndAffordances(String employeeName);

    abstract Link createListLinkAndAffordances();

    abstract WebMvcLinkBuilder getDepositFindByIdLinkBuilder(Deposit deposit);

    abstract Object createDepositRepresentation(Deposit deposit);

    abstract String handledDepositType();
}
