package com.card.nico.deposit.layers.http;

import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.ports.in.DepositUseCase;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/meal-deposits")
class MealDepositController extends BaseDepositController {


    MealDepositController(DepositUseCase depositUseCase) {
        super(depositUseCase);
    }

    @Override
    Link createFindByEmployeeLinkAndAffordances(String employeeName) {
        return linkTo(methodOn(MealDepositController.class).findByEmployee(employeeName)).withSelfRel();
    }

    @Override
    Link createListLinkAndAffordances() {
        return linkTo(methodOn(MealDepositController.class).list()).withSelfRel()
                .andAffordance(afford(methodOn(MealDepositController.class).create(null)));
    }

    @Override
    WebMvcLinkBuilder getDepositFindByIdLinkBuilder(Deposit deposit) {
        return linkTo(methodOn(MealDepositController.class).findById(deposit.getId()));
    }

    @Override
    Object createDepositRepresentation(Deposit deposit) {
        return new MealDepositController.Representation(deposit);
    }

    @Override
    String handledDepositType() {
        return "MEAL";
    }

    static class Representation extends RepresentationModel<Representation> {

        private final Long id;
        private final String companyName;
        private final String employeeName;
        private final String amount;
        private final String expirationDate;
        private final String creationDate;

        Representation(Deposit mealDeposit) {
            this.id = mealDeposit.getId();
            this.companyName = mealDeposit.getCompany().name();
            this.employeeName = mealDeposit.getEmployee().name();
            MoneyAmount moneyAmount = mealDeposit.getAmount();
            this.amount = moneyAmount.amount() + moneyAmount.currency().getCurrencyCode();
            this.expirationDate = "" + mealDeposit.getExpirationDate().orElse(null);
            this.creationDate = "" + mealDeposit.getCreationDate();

            addSelfLink();
            addEmployeeLink();
            addCompanyLink();
        }

        private void addSelfLink() {
            this.add(linkTo(methodOn(MealDepositController.class).findById(id)).withSelfRel());
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
